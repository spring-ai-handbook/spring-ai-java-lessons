package com.example.springai.lesson11.dto;

import java.util.Map;

/**
 * Lesson11 添加知识请求。
 *
 * <p>
 * text 表示真正需要进行语义向量化的知识正文，
 * metadata 表示与知识关联的业务属性，例如分类、来源等。
 * </p>
 *
 * @param text     知识正文
 * @param metadata 文档元数据，用于记录来源、分类等辅助信息
 */
public record Lesson11AddDocumentRequest(
    String text,
    Map<String, Object> metadata
) {
}
