package com.example.springai.lesson12.service;

import com.example.springai.lesson12.dto.Lesson12DocumentResult;
import com.example.springai.lesson12.dto.Lesson12SplitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Lesson12 Document 与 TextSplitter 业务服务。
 *
 * <p>
 * 本服务负责演示完整的文档预处理过程：
 * </p>
 *
 * <pre>
 * 原始文本
 *    ↓
 * Document
 *    ↓
 * TokenTextSplitter
 *    ↓
 * 多个子 Document
 * </pre>
 *
 * <p>
 * 同时提供一个将切片结果写入 VectorStore 的接口，
 * 用于把 Lesson12 与 Lesson11 学过的 VectorStore 串联起来。
 * 这里仍然只负责知识入库，不涉及 ChatModel 和完整 RAG。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class Lesson12DocumentService {

    /**
     * Lesson12 使用的 TokenTextSplitter。
     *
     * <p>
     * 它只负责文档转换和切片，
     * 不负责调用 EmbeddingModel。
     * </p>
     */
    private final TokenTextSplitter lesson12TokenTextSplitter;

    /**
     * Lesson11 已创建的统一 VectorStore。
     *
     * <p>
     * 在 Lesson12 中再次使用它，
     * 目的是把“切片”和“向量入库”连接起来。
     * </p>
     */
    private final VectorStore vectorStore;

    /**
     * 将原始文本转换成 Document 后进行切片。
     *
     * <p>
     * 注意：
     * 调用本方法不会产生 Embedding，
     * 也不会调用 OpenAI。
     * 当前阶段只是普通的本地文本处理。
     * </p>
     *
     * @param request 原始文本及 metadata
     * @return 切片后的多个 Document
     */
    public List<Lesson12DocumentResult> split(
        Lesson12SplitRequest request) {

        validateRequest(request);

        Map<String, Object> metadata =
            request.metadata() == null
                ? Map.of()
                : request.metadata();

        /*
         * 第一步：
         * 把业务中的普通 String 包装成 Spring AI Document。
         *
         * 从此以后，这段文本就可以进入 Spring AI
         * Document ETL 处理链路。
         */
        Document sourceDocument = Document.builder()
            .text(request.text())
            .metadata(metadata)
            .build();

        /*
         * 第二步：
         * 使用 TokenTextSplitter 将一个大 Document
         * 转换成多个较小 Document。
         *
         * 注意这里仍然没有 EmbeddingModel。
         */
        List<Document> chunks =
            lesson12TokenTextSplitter.split(sourceDocument);

        /*
         * 将 Spring AI Document 转换成业务 DTO，
         * 方便通过接口直接观察每个 Chunk 的内容和 metadata。
         */
        return chunks.stream()
            .map(document ->
                new Lesson12DocumentResult(
                    document.getId(),
                    document.getText(),
                    document.getMetadata()
                )
            )
            .toList();
    }

    /**
     * 对原始知识进行切片，并将所有 Chunk 写入 VectorStore。
     *
     * <p>
     * 这一步完整串起 Lesson11 和 Lesson12：
     * </p>
     *
     * <pre>
     * String
     *   ↓
     * Document
     *   ↓
     * TextSplitter
     *   ↓
     * List&lt;Document&gt;
     *   ↓
     * VectorStore.add(...)
     *   ↓
     * EmbeddingModel
     *   ↓
     * Vector
     * </pre>
     *
     * @param request 原始知识内容
     * @return 本次写入 VectorStore 的 Chunk 数量
     */
    public int splitAndStore(
        Lesson12SplitRequest request) {

        validateRequest(request);

        Map<String, Object> metadata =
            request.metadata() == null
                ? Map.of()
                : request.metadata();

        Document sourceDocument = Document.builder()
            .text(request.text())
            .metadata(metadata)
            .build();

        /*
         * 文本切片阶段：
         *
         * 不访问 OpenAI，
         * 不执行 Embedding，
         * 单纯把大文本拆成多个 Document。
         */
        List<Document> chunks =
            lesson12TokenTextSplitter.split(sourceDocument);

        /*
         * 从这一行开始才进入 Lesson11 的 VectorStore 链路。
         *
         * 每一个 Chunk 都会成为一个独立 Document，
         * VectorStore 会分别为它们生成 Embedding。
         */
        vectorStore.add(chunks);

        return chunks.size();
    }

    /**
     * 校验文本切片请求。
     *
     * @param request 请求参数
     */
    private void validateRequest(
        Lesson12SplitRequest request) {

        if (request == null
            || !StringUtils.hasText(request.text())) {

            throw new IllegalArgumentException(
                "待切片文本不能为空"
            );
        }
    }
}
