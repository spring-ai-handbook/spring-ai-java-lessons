package com.example.springai.lesson05;

/**
 * Lesson05：Structured Output 图书结构化结果。
 *
 * @param name 书名
 * @param author 作者
 * @param reason 推荐理由
 * @param difficulty 难度等级，例如 BEGINNER / INTERMEDIATE / ADVANCED
 */
public record Lesson05BookInfo(
        String name,
        String author,
        String reason,
        String difficulty
) {
}
