package com.example.springai.lesson06;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Lesson06：Streaming / Flux 学习服务。
 */
@Service
@RequiredArgsConstructor
public class Lesson06StreamingService {

    /**
     * Spring Boot 自动配置的 ChatClient.Builder。
     */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 以 Flux 形式流式返回模型生成内容。
     *
     * @param message 用户消息
     * @return 持续产生文本片段的 Flux
     */
    public Flux<String> stream(String message) {
        ChatClient chatClient = chatClientBuilder.clone().build();

        return chatClient
                .prompt()
                .system("你是一名 Java 技术助手，请使用中文清晰回答。")
                .user(message)
                .stream()
                .content();
    }
}
