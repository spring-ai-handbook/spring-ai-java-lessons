package com.example.aidemo1.lesson03;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * Lesson03：System Prompt 与 User Prompt 学习服务。
 */
@Service
@RequiredArgsConstructor
public class Lesson03PromptService {

    /**
     * Spring Boot 自动配置的 ChatClient.Builder。
     */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 使用 System Prompt 固定模型角色，再接收当前 User Prompt。
     *
     * @param message 用户当前问题
     * @return Java 学习助手的回答
     */
    public String chat(String message) {
        ChatClient chatClient = chatClientBuilder.clone().build();

        return chatClient
                .prompt()
                .system("""
                        你是一名资深 Java / Spring 开发工程师。
                        请使用中文回答，并优先给出清晰、可执行的技术解释。
                        如果用户询问与 Java 无关的问题，也可以回答，但不要编造事实。
                        """)
                .user(message)
                .call()
                .content();
    }
}
