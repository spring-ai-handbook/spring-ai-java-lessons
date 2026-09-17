package com.example.springai.lesson13.dto;

import java.util.List;

/**
 * Lesson13 RAG 最终响应。
 *
 * @param mode     当前 RAG 实现方式，manual 或 advisor
 * @param question 用户原始问题
 * @param answer   DeepSeek 根据知识上下文生成的最终答案
 * @param sources  本次 RAG 实际检索到的知识来源
 */
public record Lesson13RagResponse(
    String mode,
    String question,
    String answer,
    List<Lesson13SourceDocument> sources
) {
}
