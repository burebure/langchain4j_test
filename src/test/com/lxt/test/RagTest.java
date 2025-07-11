package com.lxt.test;

import com.lxt.test.assistant.NormalAssistant;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RagTest {

    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private EmbeddingStore embeddingStore;

    @Test
    public void test1() {
        Response<Embedding> embed = embeddingModel.embed("你好啊，我看看你怎么存储的");
        System.out.println(embed.content());
        System.out.println(embed.finishReason());
    }

    @Test
    public void test2() {
        TextSegment ts1 = TextSegment.from("我喜欢打乒乓球");
        TextSegment ts2 = TextSegment.from("我是一名程序员");
        TextSegment ts3 = TextSegment.from("我有两个小孩了");
        Embedding em1 = embeddingModel.embed(ts1).content();
        Embedding em2 = embeddingModel.embed(ts2).content();
        Embedding em3 = embeddingModel.embed(ts3).content();
        System.out.println(embeddingStore.add(em1, ts1));
        System.out.println(embeddingStore.add(em2, ts2));
        System.out.println(embeddingStore.add(em3, ts3));
    }

    @Test
    public void  test3(){
        Embedding search = embeddingModel.embed("我的爱好是啥").content();
        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .maxResults(1)
                .minScore(0.8)
                .queryEmbedding(search)
                .filter(MetadataFilterBuilder.metadataKey("test").isEqualTo("haha"))
                .build();
        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(request);
        searchResult.matches().forEach(match -> {
            System.out.println(match.embeddingId()+","+match.embedded().text());
        });
    }
    @Resource
    private NormalAssistant promptAiAssistant;

    @Test
    public  void  test4(){
        System.out.println(promptAiAssistant.chat("6", "你知道我现在有几个小孩了吗"));
    }
}
