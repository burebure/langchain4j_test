package com.lxt.test.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface NormalAssistant {
    String chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
