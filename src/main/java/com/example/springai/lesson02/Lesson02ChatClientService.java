package com.example.springai.lesson02;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * Lesson02：ChatClient Fluent API 学习服务。
 *
 * <p>ChatClient 是日常业务开发中更常使用的高级 API，
 * 可以在同一条链上配置 Prompt、Advisor、Tool、Structured Output 等能力。</p>
 */
@Service
@RequiredArgsConstructor
public class Lesson02ChatClientService {

    /**
     * Spring Boot 自动配置提供的 ChatClient.Builder。
     */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 使用 ChatClient 完成一次普通聊天请求。
     *
     * @param message 用户消息
     * @return 模型回答
     */
    public String chat(String message) {
        ChatClient chatClient = chatClientBuilder.clone().build();

        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }
}
