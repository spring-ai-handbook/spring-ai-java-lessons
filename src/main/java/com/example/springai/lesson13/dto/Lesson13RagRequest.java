package com.example.springai.lesson13.dto;

/**
 * Lesson13 RAG 问答请求。
 *
 * <p>
 * 用户除了提供自然语言问题外，还可以控制向量检索阶段的
 * topK、相似度阈值以及可选的知识分类。
 * </p>
 *
 * @param question            用户问题
 * @param topK               最多检索多少条相关知识
 * @param similarityThreshold 最低相似度阈值
 * @param category           可选知识分类，用于 metadata 过滤
 */
public record Lesson13RagRequest(
    String question,
    Integer topK,
    Double similarityThreshold,
    String category
) {
}
