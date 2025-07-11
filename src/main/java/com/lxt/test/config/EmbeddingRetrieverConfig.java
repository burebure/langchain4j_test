package com.lxt.test.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeServerlessIndexConfig;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingRetrieverConfig {

    @Resource
    private EmbeddingModel embeddingModel;

    /**
     * 注入Pinecone 向量数据库
     *
     * @return result
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        EmbeddingStore<TextSegment> embeddingStore = PineconeEmbeddingStore.builder()
                .apiKey("pcsk_41D5aD_Go9yPGJFhBzenG4vCwm9QDHbRT5n6nSTmP3AkRTSzHMq2Qicqo9PzxYmFG72yxQ")
                .index("lxt-index")//如果指定的索引不存在， 将创建一个新的索引
                .nameSpace("lxt-namespace") //如果指定的名称空间不存在， 将创建一个新的名称空间
                .createIndex(PineconeServerlessIndexConfig.builder()
                        .cloud("AWS") //指定索引部署在 AWS 云服务上。
                        .region("us-east-1") //指定索引所在的 AWS 区域为 us-east-1。
                        .dimension(embeddingModel.dimension())
                        .build())
                .build();
        return embeddingStore;
    }

    /**
     * 创建内容检索器
     *
     * @return retriever
     */
    @Bean
    public ContentRetriever contentRetriever() {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore())
                .maxResults(1)
                .minScore(0.8)
                .build();
    }

}
