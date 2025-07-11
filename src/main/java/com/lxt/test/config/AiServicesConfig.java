package com.lxt.test.config;

import com.lxt.test.assistant.NormalAssistant;
import com.lxt.test.assistant.StreamingAssistant;
import com.lxt.test.tools.WeatherTools;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiServicesConfig {

    @Resource
    private OpenAiStreamingChatModel openAiStreamingChatModel;

    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private ChatMemoryProvider chatMemoryProvider;

    @Resource
    private McpToolProvider mcpToolProvider;

    @Resource
    private WeatherTools weatherTools;

    @Resource
    private ContentRetriever contentRetriever;

    @Bean
    public StreamingAssistant streamingAssistant() {
        return AiServices.builder(StreamingAssistant.class)
                .streamingChatModel(openAiStreamingChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .toolProvider(mcpToolProvider)
                .tools(weatherTools)
                .contentRetriever(contentRetriever)
                .build();
    }

    @Bean
    public NormalAssistant normalAssistant() {
        return AiServices.builder(NormalAssistant.class)
                .chatModel(openAiChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .toolProvider(mcpToolProvider)
                .tools(weatherTools)
                .contentRetriever(contentRetriever)
                .build();
    }
}
