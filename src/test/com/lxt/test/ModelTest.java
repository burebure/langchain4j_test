package com.lxt.test;

import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ModelTest {

    @Resource
    private OpenAiChatModel openAiChatModel;

    /**
     * 使用langchain4j提供的默认大语言模型
     */
    @Test
    public void demo() {
        // 自己构造openAiChatModel
        // OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
        //         .baseUrl("http://langchain4j.dev/demo/openai/v1")
        //         .apiKey("demo")
        //         .modelName("gpt-4o-mini")
        //         .build();
        String result = openAiChatModel.chat("你是谁啊,我是一个程序员!");
        System.out.println(result);
    }

    /**
     * 调用阿里百炼大模型
     */
    @Test
    public void qwenChatTest() {
        String result = openAiChatModel.chat("你是谁，我是通过接口问的");
        System.out.println(result);
    }

    @Value("${langchain4j.community.dashscope.chat-model.api-key}")
    private String dashScopeApiKey;

    /**
     * 调用阿里百炼文生图api
     */
    @Test
    public void qwenImgTest() {
        WanxImageModel imgModel = WanxImageModel.builder()
                .modelName("wanx2.1-t2i-turbo")
                .apiKey(dashScopeApiKey)
                .build();
        //Response<Image> result = imgModel.generate("夕阳下的北方农家小院，红砖房檐下挂着玉米串和红辣椒，母亲在灶台边煮饭，炊烟融入晚霞，父亲抱着柴火走向厨房，小女孩蹲在院角喂鸡，小男孩追着橘色小猫跑过晾晒的粮食堆，远处麦田泛着金色余晖，风格为温暖光影的厚涂油画，色调偏橘黄暖色，比例3:4");
        Response<Image> result = imgModel.generate("生成一张微信头像，本人年龄33岁，男，风格不要太幼稚，图中包含火影中的漩涡鸣人，风格是 乐观、积极、向上的；图片中的鸣人最好有点沧桑感 因为本人也人到中年了，最好是嘴里叼着手里剑的那种图，比例 「1:1」");


        System.out.println(result.content().url());
    }

}
