package com.example.springai.lesson13.dto;

import java.util.Map;

/**
 * Lesson13 RAG 检索到的知识来源。
 *
 * <p>
 * 企业 RAG 不应该只返回 AI 最终答案，
 * 通常还应该保留本次回答实际使用了哪些知识，
 * 方便前端展示引用来源以及后续排查错误回答。
 * </p>
 *
 * @param id       Document 唯一标识
 * @param text     检索到的知识正文
 * @param metadata Document 元数据
 * @param score    当前知识与用户问题的向量相似度
 */
public record Lesson13SourceDocument(
    String id,
    String text,
    Map<String, Object> metadata,
    Double score
) {
}
