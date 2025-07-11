package com.lxt.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.lxt.test.assistant.NormalAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.experimental.rag.content.retriever.sql.SqlDatabaseContentRetriever;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest
public class AdvanceRagTest {

    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private ChatMemoryProvider chatMemoryProvider;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void test() {
        // 导入文案到知识库
        Document document = FileSystemDocumentLoader.loadDocument(toPath("document/人物传记.txt"), new TextDocumentParser());
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
//                .documentSplitter(DocumentSplitters.recursive(500, 100))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(document);

        // 搜索向量数据库
//        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
//                .minScore(0.9)
//                .queryEmbedding(embeddingModel.embed("感冒了应该怎么样").content())
//                .maxResults(1)
//                .build();
//        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);
//        for (EmbeddingMatch<TextSegment> item : result.matches()) {
//            System.out.println(item.embedded().text());
//            System.out.println("分数:" + item.score());
//        }


        //CompressingQueryTransformer cqTransformer = new CompressingQueryTransformer(openAiChatModel);
        //ExpandingQueryTransformer expandingQueryTransformer = new ExpandingQueryTransformer(openAiChatModel);
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(1)
                // 动态最低分
                .dynamicMinScore(query -> {
                    System.out.printf("打印一下存储id：%s,消息内容：%s%n",query.metadata().chatMemoryId(),query.text());
                    return 0.9;
                })
                .build();

        RetrievalAugmentor augmentor = DefaultRetrievalAugmentor.builder()
                //.queryTransformer(expandingQueryTransformer)
                .contentRetriever(contentRetriever)
                .build();

        NormalAssistant assistant = AiServices.builder(NormalAssistant.class)
                .chatModel(openAiChatModel)
                .retrievalAugmentor(augmentor)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
        System.out.println(assistant.chat("1", "是否可以用于膝盖？"));
    }

    @Resource
    private  DruidDataSource dataSource;

    @Test
    public  void  test2(){
        ContentRetriever contentRetriever = SqlDatabaseContentRetriever.builder()
                .dataSource(dataSource)
                .chatModel(openAiChatModel)
                .build();

        NormalAssistant assistant = AiServices.builder(NormalAssistant.class)
                .chatModel(openAiChatModel)
                .contentRetriever(contentRetriever)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
        System.out.println(assistant.chat("3","查询一下 医生最近的登录情况,需要医生姓名和医院名称，只要yn=1的，它=1 是有效的意思，你不会联查一下吗，比如查询doctor表 它的id就是doctorid"));
        // 测试一下
    }


    private Path toPath(String path) {
        try {
            URL fileUrl = AdvanceRagTest.class.getClassLoader().getResource(path);
            return Paths.get(fileUrl.toURI());
        } catch (Exception e) {
            return null;
        }
    }

//    private static DataSource createDataSource() {
//        DruidDataSource druidDataSource=new DruidDataSource();
//        druidDataSource.setUrl("jdbc:mysql://10.10.11.71:3306/czb_asset_manage?characterEncoding=UTF-8&rewriteBatchedStatements=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=Asia/Shanghai");
//        druidDataSource.setUsername("test_dev01");
//        druidDataSource.setPassword("X&PXR23OXar#R");
//        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        druidDataSource.setInitialSize(5);
//        druidDataSource.setMaxActive(20);
//        druidDataSource.setMinIdle(5);
//        druidDataSource.setMaxWait(60000);
//        return druidDataSource;
//    }
}


