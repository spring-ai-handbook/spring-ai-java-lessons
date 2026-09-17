package com.example.springai.lesson11.service;


import com.example.springai.lesson11.dto.Lesson11AddDocumentRequest;
import com.example.springai.lesson11.dto.Lesson11SearchRequest;
import com.example.springai.lesson11.dto.Lesson11SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Lesson11VectorStoreService {

    private final VectorStore vectorStore;

    public String addDocument(Lesson11AddDocumentRequest request) {
        if (request == null || !StringUtils.hasText(request.text())) {
            throw new IllegalArgumentException("text 不能为空");
        }

        /*
         * Document 的 metadata 不能传入 null。
         * 如果调用方没有提供 metadata，则使用空 Map。
         */
        Map<String, Object> objectMap = request.metadata() == null ? Map.of() : request.metadata();

        /*
         * Document 是 Spring AI 在知识处理、向量存储、
         * RAG 等流程中的核心数据载体。
         */

        Document build = Document.builder().text(request.text()).metadata(objectMap).build();

        vectorStore.add(List.of(build));

        return build.getId();
    }


    /**
     * 初始化 Lesson11 示例知识库。
     *
     * <p>
     * 使用固定 Document ID，主要是为了避免学习过程中
     * 重复调用初始化接口后不断产生相同知识的随机 ID 数据。
     * </p>
     *
     * @return 本次初始化的知识数量
     */
    public int initializeKnowledgeBase() {

        List<Document> documents = List.of(

            Document.builder()
                .id("lesson11-refund")
                .text("订单发货后7天内可以申请退款")
                .metadata(Map.of(
                    "category", "refund",
                    "source", "customer-service"
                ))
                .build(),

            Document.builder()
                .id("lesson11-member")
                .text("普通会员每月可以领取1张优惠券")
                .metadata(Map.of(
                    "category", "member",
                    "source", "customer-service"
                ))
                .build(),

            Document.builder()
                .id("lesson11-logistics")
                .text("订单发货后可以在订单详情查看物流信息")
                .metadata(Map.of(
                    "category", "logistics",
                    "source", "customer-service"
                ))
                .build(),

            Document.builder()
                .id("lesson11-service-hours")
                .text("公司客服工作时间为每天9:00到18:00")
                .metadata(Map.of(
                    "category", "service",
                    "source", "customer-service"
                ))
                .build()
        );

        /*
         * add(...) 会触发 Document 的 Embedding 生成。
         *
         * 对当前配置而言，真正执行向量生成的是：
         *
         * OpenAiEmbeddingModel
         *     ↓
         * text-embedding-3-small
         */
        vectorStore.add(documents);

        return documents.size();
    }

    /**
     * 根据用户自然语言执行语义相似度搜索。
     *
     * <p>
     * similaritySearch(...) 并不是普通数据库的 LIKE 查询。
     * VectorStore 会首先把 query 转换成查询向量，
     * 再与已经存储的 Document 向量进行相似度比较。
     * </p>
     *
     * @param request 搜索条件
     * @return 按相关度排序后的 Document 结果
     */

    public List<Lesson11SearchResult> search(Lesson11SearchRequest request) {

        validateSearchRequest(request);
        /*
         * SearchRequest 用于描述一次向量搜索需要的条件：
         *
         * query               查询文本
         * topK                最多返回数量
         * similarityThreshold 最低相似度要求
         * filterExpression    metadata 过滤条件
         */

        SearchRequest.Builder similarityThreshold = SearchRequest.builder().query(request.query()).topK(request.topK()).similarityThreshold(request.similarityThreshold());

        /*
         * 如果指定 category，则通过 metadata filter
         * 限制只在指定业务分类中进行检索。
         *
         * 这里使用 Spring AI 的 FilterExpressionBuilder，
         * 而不是自己拼字符串条件。
         */

        if (StringUtils.hasText(request.category())){
            FilterExpressionBuilder filterBuilder =
                new FilterExpressionBuilder();

            similarityThreshold.filterExpression(
                filterBuilder
                    .eq("category", request.category())
                    .build()
            );
        }
        List<Document> documents =
            vectorStore.similaritySearch(
                similarityThreshold.build()
            );

        /*
         * Spring AI Document 属于基础设施模型，
         * Controller 对外返回自己的 DTO，
         * 避免 Web API 和 Spring AI 实现细节直接绑定。
         */
        return documents.stream()
            .map(document ->
                new Lesson11SearchResult(
                    document.getId(),
                    document.getText(),
                    document.getMetadata(),
                    document.getScore()
                )
            )
            .toList();
    }

    /**
     * 校验搜索参数。
     *
     * @param request 搜索请求
     */
    private void validateSearchRequest(Lesson11SearchRequest request) {
        if (request == null || !StringUtils.hasText(request.query())) {
            throw new IllegalArgumentException("搜索内容不能为空");
        }
        if (request.topK() <= 0) {
            throw new IllegalArgumentException("topK 必须大于 0");
        }

        if (request.similarityThreshold() < 0 || request.similarityThreshold() > 1) {
            throw new IllegalArgumentException(
                "similarityThreshold 必须在 0 到 1 之间"
            );
        }
    }

}
