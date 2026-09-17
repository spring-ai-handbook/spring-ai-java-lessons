package com.example.springai.lesson10.dto;

import java.util.List;

/**
 * Lesson10 单文本 Embedding 响应对象。
 *
 * <p>
 * 用于向前端展示一段文本经过 Embedding 模型处理之后的基本结果。
 * </p>
 *
 * <p>
 * Embedding 的完整向量通常可能包含数百甚至数千个浮点数。
 * 为了避免学习接口一次返回大量无意义数字，
 * 当前 DTO 只返回：
 * </p>
 *
 * <ul>
 *     <li>原始文本</li>
 *     <li>向量维度</li>
 *     <li>向量前若干位预览</li>
 * </ul>
 *
 * @param text          原始文本
 * @param dimensions    完整向量维度
 * @param vectorPreview 向量前若干位预览数据
 */
public record Lesson10EmbeddingResponse(
    String text,
    int dimensions,
    List<Float> vectorPreview
) {
}
