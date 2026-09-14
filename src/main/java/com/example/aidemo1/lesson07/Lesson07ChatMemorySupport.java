package com.example.aidemo1.lesson07;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;

/**
 * Lesson07 专用 ChatClient 与 ChatMemory 组合对象。
 *
 * <p>使用一个独立类型封装本课需要的两个对象，
 * 避免与其他课程或 Spring Boot 自动配置的 ChatMemory Bean 发生注入歧义。</p>
 *
 * @param chatClient 已安装 Memory Advisor 的 ChatClient
 * @param chatMemory 本课使用的滑动窗口聊天记忆
 */
public record Lesson07ChatMemorySupport(
        ChatClient chatClient,
        ChatMemory chatMemory
) {
}
