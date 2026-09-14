package com.example.aidemo1.lesson05;

/**
 * Lesson05 知识点结构。
 *
 * @param name 知识点名称
 * @param description 简要说明
 * @param difficulty 难度，例如 EASY、MEDIUM、HARD
 */
public record Lesson05KnowledgePoint(
        String name,
        String description,
        String difficulty
) {
}
