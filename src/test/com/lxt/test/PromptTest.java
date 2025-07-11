package com.lxt.test;

import com.lxt.test.assistant.NormalAssistant;
import com.lxt.test.tools.WeatherTools;
import dev.langchain4j.community.model.dashscope.QwenTokenCountEstimator;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PromptTest {

    @Resource
    private  NormalAssistant normalAssistant;

    @Test
    public void test2() {
        System.out.println(normalAssistant.chat("3","查一下深圳最近三天的天气，以及规划下最近三天的游玩路线"));
        //System.out.println(normalAssistant.chat("2","查一下北京最近三天的天气，以及规划下最近三天的游玩路线"));
        //System.out.println(normalAssistant.chat("1","看一下 D:\\companyCode\\test  一共多少个文件，分别的名字是啥"));
    }


    @Test
    public void test3() {
//        qwenChatModel

        MessageWindowChatMemory.builder()
                .maxMessages(3)// 最大保留消息的数量
                .build();

        TokenWindowChatMemory.builder()
                .maxTokens(100000, new QwenTokenCountEstimator("sk-5ffd841789044e82b3a766d15bd077da", "qwen-turbo"))// 最大保留消息的token数量，这个根据模型不同而不同，并且TokenCountEstimator 也需要根据不同给的模型而设置
                .build();
//
//        TokenWindowChatMemory memory1 =
//
//        qwenChatModel.chat("你好啊");

        QwenTokenCountEstimator qwenTokenCountEstimator = new QwenTokenCountEstimator("sk-5ffd841789044e82b3a766d15bd077da", "qwen-max");
        System.out.println(qwenTokenCountEstimator.estimateTokenCountInText("您好，我今天语文考试分数是94分，数学考了100分，怎么样？"));
    }


}
