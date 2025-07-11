package com.lxt.test.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface StreamingAssistant {
    @SystemMessage(fromResource = "prompt/PromptAiAssistantSysMsg.txt")
    Flux<String> chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
