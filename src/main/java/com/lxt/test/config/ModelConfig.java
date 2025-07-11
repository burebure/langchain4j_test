package com.lxt.test.config;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Configuration
public class ModelConfig {

    private static final String BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";
    public static final String API_KEY = "sk-5ffd841789044e82b3a766d15bd077da";
    public static final String CHAT_MODEL_NAME = "qwen-turbo";
    private static final String EMBEDDING_MODEL_NAME = "text-embedding-v3";

    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .baseUrl(BASE_URL)
                .apiKey(API_KEY)
                .modelName(CHAT_MODEL_NAME)
                .logRequests(true)
                .logResponses(true)
                .maxRetries(3)
                .timeout(Duration.ofSeconds(60))
                .listeners(List.of(new ChatModelListener() {
                    @Override
                    public void onRequest(ChatModelRequestContext requestContext) {
                        String uuidValue = UUID.randomUUID().toString().replace("-","");
                        requestContext.attributes().put("TraceID",uuidValue);
                        log.info("请求参数requestContext:{}", requestContext+"\t"+uuidValue);
                    }

                    @Override
                    public void onResponse(ChatModelResponseContext responseContext) {
                        Object object = responseContext.attributes().get("TraceID");
                        log.info("返回结果responseContext:{}", object);
                    }

                    @Override
                    public void onError(ChatModelErrorContext errorContext) {
                        log.error("请求异常ChatModelErrorContext:{}", errorContext);
                    }
                }))
                .build();
    }

    @Bean
    public OpenAiStreamingChatModel openAiStreamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl(BASE_URL)
                .apiKey(API_KEY)
                .modelName(CHAT_MODEL_NAME)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OpenAiEmbeddingModel.builder()
                .baseUrl(BASE_URL)
                .apiKey(API_KEY)
                .modelName(EMBEDDING_MODEL_NAME)
                .dimensions(1024)
                .build();
    }
}
