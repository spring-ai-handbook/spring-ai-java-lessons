package com.example.springai.lesson11.controller;

import com.example.springai.lesson11.dto.Lesson11AddDocumentRequest;
import com.example.springai.lesson11.dto.Lesson11SearchRequest;
import com.example.springai.lesson11.dto.Lesson11SearchResult;
import com.example.springai.lesson11.service.Lesson11VectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Lesson11 VectorStore REST 接口。
 *
 * <p>
 * 提供知识写入、示例知识初始化以及语义检索三个接口，
 * 用于完整演示 Spring AI VectorStore 的基本工作流程。
 * </p>
 */
@RestController
@RequestMapping("/lesson11")
@RequiredArgsConstructor
public class Lesson11VectorStoreController {

    /**
     * Lesson11 VectorStore 业务服务。
     */
    private final Lesson11VectorStoreService vectorStoreService;

    /**
     * 添加单条知识。
     *
     * @param request 知识正文与 metadata
     * @return 新增 Document 的唯一 ID
     */
    @PostMapping("/documents")
    public Map<String, Object> addDocument(
        @RequestBody Lesson11AddDocumentRequest request) {

        String documentId =
            vectorStoreService.addDocument(request);

        return Map.of(
            "documentId", documentId,
            "message", "知识添加成功"
        );
    }

    /**
     * 初始化 Lesson11 示例知识库。
     *
     * @return 初始化知识数量
     */
    @PostMapping("/documents/init")
    public Map<String, Object> initializeKnowledgeBase() {

        int count =
            vectorStoreService.initializeKnowledgeBase();

        return Map.of(
            "count", count,
            "message", "Lesson11 示例知识库初始化成功"
        );
    }

    /**
     * 使用自然语言进行向量语义搜索。
     *
     * <p>
     * threshold 默认使用 0，
     * 这样第一次学习时不会因为阈值设置过高导致完全看不到结果。
     * 等确认语义搜索正常以后，再逐步测试 0.4、0.5、0.6 等阈值。
     * </p>
     *
     * @param query     用户问题
     * @param topK      最大返回数量，默认 3
     * @param threshold 最低相似度阈值，默认 0
     * @param category  可选 metadata 分类过滤条件
     * @return 搜索结果
     */
    @GetMapping("/search")
    public List<Lesson11SearchResult> search(
        @RequestParam String query,
        @RequestParam(defaultValue = "3") int topK,
        @RequestParam(defaultValue = "0.0") double threshold,
        @RequestParam(required = false) String category) {

        Lesson11SearchRequest request =
            new Lesson11SearchRequest(
                query,
                topK,
                threshold,
                category
            );

        return vectorStoreService.search(request);
    }
}
