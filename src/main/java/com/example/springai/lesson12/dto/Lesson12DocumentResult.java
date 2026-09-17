package com.example.springai.lesson12.dto;

import java.util.Map;

/**
 * Lesson12 Document 切片结果。
 *
 * <p>
 * 用课程自己的 DTO 返回数据，
 * 避免 REST API 直接暴露 Spring AI Document 类型。
 * </p>
 *
 * @param id       切片 Document 的唯一 ID
 * @param text     当前 Chunk 的文本内容
 * @param metadata 当前 Chunk 的元数据
 */
public record Lesson12DocumentResult(
    String id,
    String text,
    Map<String, Object> metadata
) {
}
