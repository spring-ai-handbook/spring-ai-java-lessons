package com.example.springai.lesson11.config;


import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Lesson11 VectorStore 配置类。
 *
 * <p>
 * 本课程暂时不接入 PostgreSQL、Redis、Milvus、Qdrant 等外部向量数据库，
 * 而是使用 Spring AI 提供的 {@link SimpleVectorStore} 内存实现，
 * 目的是把学习重点放在 Document、Embedding、VectorStore 和相似度搜索本身。
 * </p>
 *
 * <p>
 * SimpleVectorStore 在创建时需要一个 {@link EmbeddingModel}。
 * 当前项目中的 EmbeddingModel 由 Spring AI OpenAI 自动配置提供，
 * 实际底层使用 OpenAI text-embedding-3-small。
 * </p>
 */
@Configuration
public class Lesson11VectorStoreConfig {


    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {

        return SimpleVectorStore.builder(embeddingModel)
            .build();
    }

}
