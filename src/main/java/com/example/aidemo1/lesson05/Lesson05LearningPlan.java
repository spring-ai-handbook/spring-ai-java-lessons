package com.example.aidemo1.lesson05;

import java.util.List;

/**
 * Lesson05 结构化学习计划。
 *
 * @param technology 技术名称
 * @param summary 学习计划概要
 * @param steps 具体学习步骤
 */
public record Lesson05LearningPlan(
        String technology,
        String summary,
        List<String> steps
) {
}
