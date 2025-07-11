package com.lxt.test;

import com.lxt.test.assistant.NormalAssistant;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.annotation.Resource;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemoryTest {
    @Resource
    private OpenAiChatModel openAiChatModel;

    @Test
    public  void test1()
    {
        UserMessage userMessage = UserMessage.userMessage("我是啸天");
        ChatResponse chat = openAiChatModel.chat(userMessage);
        AiMessage result = chat.aiMessage();
        System.out.println(result.text());

        UserMessage message2 = UserMessage.userMessage("这次知道我是谁了吗");
        ChatResponse result2 = openAiChatModel.chat(Lists.newArrayList(userMessage, message2));
        System.out.println(result2.aiMessage().text());
    }

    @Resource
    private NormalAssistant myAiAssistant;
    @Test
    public void  test2(){
//        System.out.println("1、"+myAiAssistant.chat("我是啸天，记住我的名字,我是一名程序员,我喜欢打球，我有3个孩子了"));
//        System.out.println("2、"+myAiAssistant.chat("我是谁，说一下我的名字"));
//        System.out.println("3、"+myAiAssistant.chat("说一下我的职业"));
//        System.out.println("4、"+myAiAssistant.chat("我的爱好是啥"));
//        System.out.println("5、"+myAiAssistant.chat("我现在有小孩了吗"));
    }


}
