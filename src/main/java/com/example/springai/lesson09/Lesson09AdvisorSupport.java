package com.example.springai.lesson09;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;

/**
 * Lesson09 专用 ChatClient 与 ChatMemory 组合对象。
 *
 * @param chatClient 已安装 Lesson09 Advisor 的 ChatClient
 * @param chatMemory 本课使用的 ChatMemory
 */
public record Lesson09AdvisorSupport(
        ChatClient chatClient,
        ChatMemory chatMemory
) {
}
