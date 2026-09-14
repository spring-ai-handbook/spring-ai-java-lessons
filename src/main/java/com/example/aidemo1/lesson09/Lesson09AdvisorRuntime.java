package com.example.aidemo1.lesson09;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;

/**
 * Lesson09：封装本课程专用的 ChatClient 和 ChatMemory。
 *
 * <p>使用独立 Runtime 对象可以避免和 Lesson07 的 ChatMemory Bean 混淆，
 * 同时 Controller / Service 不需要使用 @Qualifier，也更适合配合 Lombok 构造器注入。</p>
 *
 * @param chatClient 安装了 Lesson09 Advisors 的 ChatClient
 * @param chatMemory Lesson09 独立 Chat Memory
 */
public record Lesson09AdvisorRuntime(
        ChatClient chatClient,
        ChatMemory chatMemory
) {
}
