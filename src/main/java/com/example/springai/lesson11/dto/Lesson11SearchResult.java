package com.example.springai.lesson11.dto;

import java.util.Map;

/**
 * Lesson11 向量搜索结果。
 *
 * <p>
 * 不直接把 Spring AI 的 Document 暴露给 Controller，
 * 而是转换成课程自己的 DTO。
 * 这样可以避免业务接口与 Spring AI 内部对象产生过强耦合。
 * </p>
 *
 * @param id       Document 唯一标识
 * @param text     Document 文本内容
 * @param metadata Document 元数据
 * @param score    当前 Document 与用户查询之间的相似度得分
 */
public record Lesson11SearchResult(
    String id,
    String text,
    Map<String, Object> metadata,
    Double score
) {
}
