package com.example.springai.lesson13.service;

import com.example.springai.lesson13.dto.Lesson13RagRequest;
import com.example.springai.lesson13.dto.Lesson13RagResponse;
import com.example.springai.lesson13.dto.Lesson13SourceDocument;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Lesson13 RAG 核心业务服务。
 *
 * <p>
 * 本课程同时实现两种 RAG：
 * </p>
 *
 * <pre>
 * 1. 手动 RAG
 *    VectorStore 检索
 *    → 手工构造 Context
 *    → DeepSeek
 *
 * 2. QuestionAnswerAdvisor RAG
 *    由 Spring AI Advisor 自动完成检索和 Prompt 增强
 * </pre>
 *
 * <p>
 * 通过同时实现两种方式，可以清楚理解：
 * QuestionAnswerAdvisor 并不是新的 AI 模型，
 * 而是对标准 RAG 流程的一层封装。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class Lesson13RagService {

    /**
     * 当前项目的聊天模型。
     *
     * <p>
     * 根据项目配置，实际注入的是 DeepSeek ChatModel。
     * DeepSeek 负责读取检索到的知识并生成最终自然语言答案。
     * </p>
     */
    private final ChatModel chatModel;

    /**
     * Spring AI 统一向量存储接口。
     *
     * <p>
     * 当前 Lesson11 使用的是 SimpleVectorStore。
     * 后续替换 PGVector、Milvus、Qdrant 等实现后，
     * 本 Service 仍然可以继续依赖 VectorStore 抽象。
     * </p>
     */
    private final VectorStore vectorStore;

    /**
     * Lesson13 使用的基础 ChatClient。
     *
     * <p>
     * 它本身没有默认配置 RAG Advisor。
     * 手动 RAG 和 Advisor RAG 都基于此 ChatClient 发起模型请求。
     * </p>
     */
    private ChatClient chatClient;

    /**
     * 初始化 Lesson13 ChatClient。
     *
     * <p>
     * 这里不是单纯的依赖注入构造器，
     * 而是在所有 Spring 依赖注入完成后，
     * 基于 ChatModel 创建本课程自己的 ChatClient。
     * </p>
     */
    @PostConstruct
    public void initializeChatClient() {

        this.chatClient = ChatClient.builder(chatModel)
            .build();
    }

    /**
     * 初始化 Lesson13 演示知识。
     *
     * <p>
     * 这里故意使用虚构商城规则，
     * 避免 DeepSeek 本身已经知道这些答案，
     * 从而可以更明显地验证 RAG 是否真正工作。
     * </p>
     *
     * @return 初始化的知识数量
     */
    public int initializeKnowledgeBase() {

        List<Document> documents = List.of(

            Document.builder()
                .id("lesson13-refund")
                .text("""
                                星河商城2026年售后规则规定：
                                已发货订单在签收后的7天内可以申请无理由退款，
                                但已经明显使用并影响二次销售的商品除外。
                                """)
                .metadata(Map.of(
                    "category", "refund",
                    "source", "xinghe-policy-2026"
                ))
                .build(),

            Document.builder()
                .id("lesson13-black-member")
                .text("""
                                星河商城黑金会员权益规定：
                                黑金会员每个自然月可以领取5张无门槛优惠券，
                                优惠券必须在当月使用，过期后自动失效。
                                """)
                .metadata(Map.of(
                    "category", "member",
                    "source", "xinghe-member-2026"
                ))
                .build(),

            Document.builder()
                .id("lesson13-logistics")
                .text("""
                                星河商城物流规则规定：
                                商品发货后，用户可以进入订单详情页面查看实时物流信息。
                                如果物流超过48小时没有更新，可以申请客服介入处理。
                                """)
                .metadata(Map.of(
                    "category", "logistics",
                    "source", "xinghe-logistics-2026"
                ))
                .build(),

            Document.builder()
                .id("lesson13-service")
                .text("""
                                星河商城人工客服工作时间为每天上午9点到晚上18点。
                                18点以后可以提交在线工单，
                                工单将在下一个工作时段按照提交顺序处理。
                                """)
                .metadata(Map.of(
                    "category", "service",
                    "source", "xinghe-service-2026"
                ))
                .build()
        );

        /*
         * Lesson11 已经学过：
         *
         * vectorStore.add(...)
         *      ↓
         * EmbeddingModel
         *      ↓
         * OpenAI text-embedding-3-small
         *      ↓
         * Vector
         *      ↓
         * VectorStore
         */
        vectorStore.add(documents);

        return documents.size();
    }

    /**
     * 手动实现一次完整 RAG。
     *
     * <p>
     * 这个方法非常重要。
     * 我们暂时不使用 QuestionAnswerAdvisor，
     * 而是把 RAG 的 Retrieval、Augmentation、Generation
     * 三个阶段全部手工写出来。
     * </p>
     *
     * @param request RAG 请求
     * @return AI 回答以及检索到的知识来源
     */
    public Lesson13RagResponse manualRag(
        Lesson13RagRequest request) {

        validateRequest(request);

        int topK = getTopK(request);
        double threshold = getThreshold(request);

        /*
         * ==============================
         * 第一阶段：Retrieval
         * ==============================
         *
         * 先构造 VectorStore 查询条件。
         */
        SearchRequest.Builder searchRequestBuilder =
            SearchRequest.builder()
                .query(request.question())
                .topK(topK)
                .similarityThreshold(threshold);

        /*
         * 如果指定知识分类，
         * 使用 metadata filter 缩小检索范围。
         */
        if (StringUtils.hasText(request.category())) {

            FilterExpressionBuilder filterBuilder =
                new FilterExpressionBuilder();

            searchRequestBuilder.filterExpression(
                filterBuilder
                    .eq("category", request.category())
                    .build()
            );
        }

        /*
         * similaritySearch 内部会：
         *
         * 用户问题
         *    ↓
         * EmbeddingModel
         *    ↓
         * Query Vector
         *    ↓
         * VectorStore 相似度搜索
         *    ↓
         * 返回最相关 Document
         */
        List<Document> documents =
            vectorStore.similaritySearch(
                searchRequestBuilder.build()
            );

        /*
         * 企业项目中非常推荐：
         *
         * 如果完全没有检索到有效知识，
         * 可以直接停止调用 LLM，
         * 防止模型脱离企业知识自行编答案，
         * 同时还能减少一次大模型调用成本。
         */
        if (documents.isEmpty()) {

            return new Lesson13RagResponse(
                "manual",
                request.question(),
                "知识库中没有找到足够相关的信息，暂时无法回答该问题。",
                List.of()
            );
        }

        /*
         * ==============================
         * 第二阶段：Augmentation
         * ==============================
         *
         * 把检索到的多个 Document 文本合并成 Context。
         */
        String context = documents.stream()
            .map(Document::getText)
            .filter(Objects::nonNull)
            .collect(
                Collectors.joining(
                    "\n\n--------------------\n\n"
                )
            );

        /*
         * 把知识库 Context 和用户原始问题组合起来。
         *
         * 这一步就是 RAG 中的“增强”。
         */
        String augmentedUserPrompt = """
                请严格根据下面【知识库上下文】回答用户问题。

                回答规则：
                1. 只能依据知识库上下文回答。
                2. 不要使用知识库以外的信息补充事实。
                3. 如果上下文无法回答问题，请明确回答“知识库中没有足够信息”。
                4. 回答使用简洁、自然的中文。

                【知识库上下文】

                %s

                【用户问题】

                %s
                """.formatted(
            context,
            request.question()
        );

        /*
         * ==============================
         * 第三阶段：Generation
         * ==============================
         *
         * 此时才真正调用 DeepSeek。
         *
         * 注意：
         * DeepSeek 并没有直接访问 VectorStore。
         *
         * DeepSeek 看到的是已经被我们增强过的 Prompt：
         *
         * Context + Question
         */
        String answer =
            chatClient.prompt()
                .user(augmentedUserPrompt)
                .call()
                .content();

        return new Lesson13RagResponse(
            "manual",
            request.question(),
            answer,
            convertSources(documents)
        );
    }

    /**
     * 使用 Spring AI QuestionAnswerAdvisor 实现 RAG。
     *
     * <p>
     * QuestionAnswerAdvisor 会自动完成：
     * </p>
     *
     * <pre>
     * 用户问题
     *      ↓
     * VectorStore 相似度搜索
     *      ↓
     * 获取 Document
     *      ↓
     * 把 Document 内容加入用户 Prompt
     *      ↓
     * ChatModel
     * </pre>
     *
     * @param request RAG 请求
     * @return AI 回答以及 Advisor 检索到的知识来源
     */
    public Lesson13RagResponse advisorRag(
        Lesson13RagRequest request) {

        validateRequest(request);

        int topK = getTopK(request);
        double threshold = getThreshold(request);

        /*
         * QuestionAnswerAdvisor 使用 SearchRequest
         * 控制向量检索参数。
         *
         * 这里不需要设置 query。
         * Advisor 会使用当前用户问题作为向量搜索 query。
         */
        SearchRequest.Builder searchRequestBuilder =
            SearchRequest.builder()
                .topK(topK)
                .similarityThreshold(threshold);

        /*
         * 可选 metadata 过滤。
         */
        if (StringUtils.hasText(request.category())) {

            FilterExpressionBuilder filterBuilder =
                new FilterExpressionBuilder();

            searchRequestBuilder.filterExpression(
                filterBuilder
                    .eq("category", request.category())
                    .build()
            );
        }

        /*
         * 自定义 QuestionAnswerAdvisor 的 RAG Prompt。
         *
         * Spring AI 2.0.1 要求自定义模板包含两个变量：
         *
         * {query}
         *      用户问题
         *
         * {question_answer_context}
         *      VectorStore 检索到的知识上下文
         */
        PromptTemplate ragPromptTemplate =
            PromptTemplate.builder()
                .template("""
                                {query}

                                下面是从企业知识库中检索到的上下文：

                                --------------------
                                {question_answer_context}
                                --------------------

                                请严格遵守以下规则：

                                1. 只能根据上面的知识库上下文回答问题。
                                2. 不要使用知识库以外的信息补充事实。
                                3. 如果上下文中没有答案，请回答：
                                   “知识库中没有足够信息。”
                                4. 不需要说“根据上下文”或“根据提供的信息”。
                                5. 使用简洁、自然的中文回答。
                                """)
                .build();

        /*
         * 创建本次请求对应的 QuestionAnswerAdvisor。
         *
         * 因为 topK、threshold、metadata filter
         * 都可能由当前请求动态决定，
         * 所以这里不把它做成一个全局固定配置。
         */
        QuestionAnswerAdvisor advisor =
            QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(
                    searchRequestBuilder.build()
                )
                .promptTemplate(ragPromptTemplate)
                .build();

        /*
         * 注意这里使用 chatClientResponse()，
         * 而不是直接 content()。
         *
         * 原因是：
         * 除了最终 AI 回答以外，
         * 我们还希望从 Advisor Context 中取得
         * 本次实际检索到的 Document。
         */
        ChatClientResponse response =
            chatClient.prompt()
                .user(request.question())
                .advisors(advisor)
                .call()
                .chatClientResponse();

        /*
         * 获取 DeepSeek 最终生成的自然语言答案。
         */
        String answer = extractAnswer(response);

        /*
         * QuestionAnswerAdvisor 会把检索到的 Document
         * 保存到 Advisor Context 中。
         *
         * Spring AI 已经公开提供：
         *
         * QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS
         *
         * 作为获取这些 Document 的标准 Context Key。
         */
        List<Document> retrievedDocuments =
            extractRetrievedDocuments(response);

        return new Lesson13RagResponse(
            "advisor",
            request.question(),
            answer,
            convertSources(retrievedDocuments)
        );
    }

    /**
     * 从 ChatClientResponse 中提取 AI 最终回答。
     *
     * @param response ChatClient 完整响应
     * @return AI 文本回答
     */
    private String extractAnswer(
        ChatClientResponse response) {

        if (response == null
            || response.chatResponse() == null
            || response.chatResponse().getResult() == null
            || response.chatResponse()
            .getResult()
            .getOutput() == null) {

            return "";
        }

        String text =
            response.chatResponse()
                .getResult()
                .getOutput()
                .getText();

        return text == null ? "" : text;
    }

    /**
     * 从 QuestionAnswerAdvisor Context 中提取检索结果。
     *
     * @param response ChatClient 完整响应
     * @return Advisor 实际检索到的 Document
     */
    private List<Document> extractRetrievedDocuments(
        ChatClientResponse response) {

        if (response == null
            || response.context() == null) {

            return List.of();
        }

        Object value = response.context().get(
            QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS
        );

        if (!(value instanceof List<?> list)) {
            return List.of();
        }

        /*
         * Context 的 value 类型是 Object，
         * 因此这里进行安全类型判断，
         * 只保留真正的 Document。
         */
        return list.stream()
            .filter(Document.class::isInstance)
            .map(Document.class::cast)
            .toList();
    }

    /**
     * 将 Spring AI Document 转换为业务层知识来源 DTO。
     *
     * @param documents 检索到的 Document
     * @return API 对外返回的知识来源列表
     */
    private List<Lesson13SourceDocument> convertSources(
        List<Document> documents) {

        return documents.stream()
            .map(document ->
                new Lesson13SourceDocument(
                    document.getId(),
                    document.getText(),
                    document.getMetadata(),
                    document.getScore()
                )
            )
            .toList();
    }

    /**
     * 获取 topK。
     *
     * <p>
     * 本课程默认返回最多 3 条知识。
     * 正式项目需要通过真实问题集进行效果评估。
     * </p>
     */
    private int getTopK(
        Lesson13RagRequest request) {

        if (request.topK() == null) {
            return 3;
        }

        if (request.topK() <= 0) {
            throw new IllegalArgumentException(
                "topK 必须大于 0"
            );
        }

        return request.topK();
    }

    /**
     * 获取相似度阈值。
     *
     * <p>
     * 教学阶段默认设置为 0.3，
     * 主要用于减少明显无关知识进入 Prompt。
     * 生产环境不应该直接照搬该数值。
     * </p>
     */
    private double getThreshold(
        Lesson13RagRequest request) {

        if (request.similarityThreshold() == null) {
            return 0.3D;
        }

        double threshold =
            request.similarityThreshold();

        if (threshold < 0D || threshold > 1D) {
            throw new IllegalArgumentException(
                "similarityThreshold 必须在 0 到 1 之间"
            );
        }

        return threshold;
    }

    /**
     * 校验 RAG 用户问题。
     *
     * @param request RAG 请求
     */
    private void validateRequest(
        Lesson13RagRequest request) {

        if (request == null
            || !StringUtils.hasText(
            request.question())) {

            throw new IllegalArgumentException(
                "用户问题不能为空"
            );
        }
    }
}
