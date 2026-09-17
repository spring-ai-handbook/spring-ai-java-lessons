package com.example.springai.lesson12.dto;

import java.util.Map;

/**
 * Lesson12 文本切片请求。
 *
 * <p>
 * text 表示待处理的原始大文本；
 * metadata 表示原始 Document 的业务信息。
 * TextSplitter 在生成子 Document 时会保留这些 metadata，
 * 从而保证切片以后仍然知道知识的原始来源。
 * </p>
 *
 * @param text     需要切片的原始文本
 * @param metadata 原始 Document 元数据
 */
public record Lesson12SplitRequest(
    String text,
    Map<String, Object> metadata
) {
}
