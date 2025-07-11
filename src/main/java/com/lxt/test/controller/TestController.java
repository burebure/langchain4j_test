package com.lxt.test.controller;

import com.lxt.test.assistant.StreamingAssistant;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class TestController {


    @Resource
    private OpenAiStreamingChatModel openAiStreamingChatModel;

    @Resource
    private StreamingAssistant streamingAssistant;

    @GetMapping("/test1")

    public Flux<String> test1(){
        return Flux.create(sink -> {
            openAiStreamingChatModel.chat("给我讲个笑话", new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    sink.next(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    sink.complete();
                }

                @Override
                public void onError(Throwable error) {
                     sink.error(error);
                }
            });
        });
    }

    @GetMapping("/test2")
    public void  test2(){
        openAiStreamingChatModel.chat("给我讲2个笑话", new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                System.out.println(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                System.out.println(completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
    }

    @GetMapping("test3")
    public  Flux<String> test3(){
        return  streamingAssistant.chat("1","你叫什么名字,你有几个小孩了");
    }
}
