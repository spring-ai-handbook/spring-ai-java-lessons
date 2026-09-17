package com.example.springai.lesson10.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Lesson10 文本语义相似度比较请求。
 *
 * <p>
 * 接口会分别把 leftText 和 rightText 转换成 Embedding 向量，
 * 然后使用余弦相似度比较两个向量之间的语义接近程度。
 * </p>
 *
 * @param leftText  第一段待比较文本
 * @param rightText 第二段待比较文本
 */
public record Lesson10SimilarityRequest(

    @NotBlank(message = "leftText 不能为空")
    String leftText,

    @NotBlank(message = "rightText 不能为空")
    String rightText
) {
}
