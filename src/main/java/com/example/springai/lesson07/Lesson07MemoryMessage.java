package com.example.springai.lesson07;

/**
 * Lesson07：对外展示的 Chat Memory 消息。
 *
 * @param type 消息类型，例如 USER / ASSISTANT / SYSTEM
 * @param text 消息文本
 */
public record Lesson07MemoryMessage(
        String type,
        String text
) {
}
