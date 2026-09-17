package com.example.springai.lesson10.dto;

/**
 * Lesson10 文本语义相似度响应对象。
 *
 * <p>
 * similarity 为两个 Embedding 向量之间计算得到的余弦相似度。
 * </p>
 *
 * <p>
 * 一般情况下：
 * </p>
 *
 * <pre>
 * 数值越接近 1
 * ↓
 * 两段文本在当前 Embedding 模型中的语义越接近
 * </pre>
 *
 * <p>
 * 注意：不能把某一个固定分数直接理解成业务上的“绝对相似”。
 * 实际项目中的阈值应该结合所使用的 Embedding 模型和业务数据进行测试。
 * </p>
 *
 * @param leftText   第一段文本
 * @param rightText  第二段文本
 * @param similarity 余弦相似度
 */
public record Lesson10SimilarityResponse(
    String leftText,
    String rightText,
    double similarity
) {
}
