package com.example.springai.lesson11.dto;

/**
 * Lesson11 向量搜索请求。
 *
 * <p>
 * 将用户输入、返回数量、相似度阈值以及可选分类过滤条件
 * 封装成独立 DTO，避免 Service 层直接依赖 HTTP 请求参数。
 * </p>
 *
 * @param query               用户自然语言查询
 * @param topK                最多返回多少条相似 Document
 * @param similarityThreshold 最低相似度阈值，范围为 0 到 1
 * @param category            可选知识分类，用于演示 metadata filter
 */
public record Lesson11SearchRequest(
    String query,
    int topK,
    double similarityThreshold,
    String category
) {
}
