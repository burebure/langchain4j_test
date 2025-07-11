package com.lxt.test.config;

import com.lxt.test.entity.MyChatMessage;
import com.mongodb.client.result.UpdateResult;
import dev.langchain4j.community.model.dashscope.QwenTokenCountEstimator;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@Slf4j
@Configuration
public class ChatMemoryProviderConfig {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * mongodb存储
     *
     * @return result
     */
    @Bean
    public ChatMemoryStore chatMemoryStore() {
        return new ChatMemoryStore() {
            @Override
            public List<ChatMessage> getMessages(Object memoryId) {
                MyChatMessage myChatMessage = mongoTemplate.findById(memoryId, MyChatMessage.class);
                if (myChatMessage != null && StringUtils.isNotBlank(myChatMessage.getContent())) {
                    return ChatMessageDeserializer.messagesFromJson(myChatMessage.getContent());
                }
                return Lists.newArrayList();
            }

            @Override
            public void updateMessages(Object memoryId, List<ChatMessage> messages) {
                UpdateResult upsertResult = mongoTemplate.upsert(new Query(Criteria.where("id").is(memoryId)), new Update().set("content", ChatMessageSerializer.messagesToJson(messages)), MyChatMessage.class);
                log.info("upsertResult: {}", upsertResult);
            }

            @Override
            public void deleteMessages(Object memoryId) {
                mongoTemplate.remove(new Query(Criteria.where("id").is(memoryId)), MyChatMessage.class);
            }
        };
    }

    /**
     * 聊天内容提供者
     *
     * @return result
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {

        return  memoryId -> TokenWindowChatMemory.builder()
                .id(memoryId)
                .maxTokens(4000, new QwenTokenCountEstimator(ModelConfig.API_KEY, ModelConfig.CHAT_MODEL_NAME))
                .chatMemoryStore(chatMemoryStore())
                .build();

//        return  memoryId -> MessageWindowChatMemory.builder()
//                .chatMemoryStore(chatMemoryStore())
//                .maxMessages(20)
//                .build();
    }
}
