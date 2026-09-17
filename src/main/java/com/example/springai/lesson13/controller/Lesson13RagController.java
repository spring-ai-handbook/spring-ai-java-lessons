package com.example.springai.lesson13.controller;

import com.example.springai.lesson13.dto.Lesson13RagRequest;
import com.example.springai.lesson13.dto.Lesson13RagResponse;
import com.example.springai.lesson13.service.Lesson13RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Lesson13 RAG 对外 REST 接口。
 *
 * <p>
 * 提供知识初始化、手动 RAG 和 Spring AI Advisor RAG，
 * 用于完整观察检索增强生成的工作过程。
 * </p>
 */
@RestController
@RequestMapping("/lesson13")
@RequiredArgsConstructor
public class Lesson13RagController {

    /**
     * Lesson13 RAG 核心服务。
     */
    private final Lesson13RagService ragService;

    /**
     * 初始化 Lesson13 演示知识库。
     *
     * @return 初始化结果
     */
    @PostMapping("/knowledge/init")
    public Map<String, Object> initializeKnowledgeBase() {

        int count =
            ragService.initializeKnowledgeBase();

        return Map.of(
            "count", count,
            "message", "Lesson13 RAG 演示知识初始化成功"
        );
    }

    /**
     * 使用手写方式完成一次 RAG。
     *
     * <p>
     * 此接口主要用于学习 RAG 内部完整流程，
     * 可以直接看到 Retrieval、Augmentation、
     * Generation 三个阶段。
     * </p>
     *
     * @param request 用户问题及向量检索参数
     * @return AI 回答与知识来源
     */
    @PostMapping("/manual")
    public Lesson13RagResponse manualRag(
        @RequestBody Lesson13RagRequest request) {

        return ragService.manualRag(request);
    }

    /**
     * 使用 Spring AI QuestionAnswerAdvisor 完成 RAG。
     *
     * @param request 用户问题及向量检索参数
     * @return AI 回答与 Advisor 检索到的知识来源
     */
    @PostMapping("/advisor")
    public Lesson13RagResponse advisorRag(
        @RequestBody Lesson13RagRequest request) {

        return ragService.advisorRag(request);
    }
}
