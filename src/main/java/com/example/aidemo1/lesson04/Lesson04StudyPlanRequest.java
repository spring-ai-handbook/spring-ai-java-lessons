package com.example.aidemo1.lesson04;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * Lesson04：PromptTemplate 学习计划请求。
 *
 * @param technology 需要学习的技术
 * @param years Java 开发经验年限
 * @param days 计划学习天数
 */
public record Lesson04StudyPlanRequest(
        @NotBlank(message = "technology 不能为空") String technology,
        @Min(value = 0, message = "years 不能小于 0") int years,
        @Min(value = 1, message = "days 至少为 1") int days
) {
}
