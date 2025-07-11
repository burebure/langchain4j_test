package com.lxt.test;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenCountEstimator;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.List;

@SpringBootTest
public class DocumentTest {

    @Resource
    private EmbeddingModel embeddingModel;
    /**
     * 加载1个txt文档
     */
    @Test
    public void test1() {
        Document document = FileSystemDocumentLoader.loadDocument("D:\\学习资料\\llm\\硅谷小智\\knowledge\\科室信息.txt");
        System.out.println(document.metadata());
        System.out.println(document.text());
    }

    /**
     * 加载多个txt文档
     */
    @Test
    public void test2() {
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**.txt");
        List<Document> list = FileSystemDocumentLoader.loadDocuments("D:\\学习资料\\llm\\硅谷小智\\knowledge", pathMatcher, new TextDocumentParser());
        for (Document document : list) {
            System.out.println(document.metadata());
//            System.out.println(document.text());
        }
    }

    /**
     * 加载多个pdf文档
     */
    @Test
    public void test3() {
        // 创建一个路径匹配器，用于匹配 PDF 文件
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**.pdf");
        List<Document> list = FileSystemDocumentLoader.loadDocuments("D:\\学习资料\\llm\\硅谷小智\\knowledge", pathMatcher, new ApacheTikaDocumentParser());
        for (Document document : list) {
            System.out.println(document.metadata());
            System.out.println(document.text());
        }
    }

    /**
     * 分割器测试 根据段落分
     */
    @Test
    public void test4() {
        DocumentByParagraphSplitter splitter = new DocumentByParagraphSplitter(300, 10, new HuggingFaceTokenCountEstimator());

        List<TextSegment> split = splitter.split(FileSystemDocumentLoader.loadDocument("D:\\学习资料\\llm\\硅谷小智\\knowledge\\科室信息.txt"));
        for (TextSegment textSegment : split) {
            System.out.println(textSegment.text());
            System.out.println("-------------------------------------------");
        }
    }

    @Test
    public void test5() {
        // 获取文档
        Document document = FileSystemDocumentLoader.loadDocument("D:\\学习资料\\llm\\硅谷小智\\knowledge\\科室信息.txt");
        // 向量数据库
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        // 文档分割器
        DocumentByParagraphSplitter splitter = new DocumentByParagraphSplitter(300, 10, new HuggingFaceTokenCountEstimator());

        IngestionResult result = EmbeddingStoreIngestor.builder().embeddingStore(embeddingStore).documentSplitter(splitter).build().ingest(document);

        System.out.println("导入结果:" + result.tokenUsage());
    }
}
