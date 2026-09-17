package com.example.springai.lesson09;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Lesson09：Advisor 调用链配置。
 */
@Configuration
public class Lesson09AdvisorConfig {

    /**
     * 创建 Lesson09 专用运行时对象。
     *
     * <p>Advisor 顺序：</p>
     *
     * <pre>
     * LoggingAdvisor          +100
     * UserContextAdvisor      +150
     * MessageChatMemoryAdvisor+200（Spring AI 默认）
     * ToolCallingAdvisor      +300（ChatClient 自动注册）
     * ChatModel
     * </pre>
     *
     * @param builder Spring Boot 自动配置的 ChatClient.Builder
     * @param loggingAdvisor 自定义日志 Advisor
     * @param userContextAdvisor 自定义用户上下文 Advisor
     * @return Lesson09 独立运行时
     */
    /**
     * 创建 Lesson09 独立运行时，避免影响前面课程的 ChatClient 配置。
     *
     * @return Lesson09 Advisor 运行时
     */
    @Bean
    public Lesson09AdvisorRuntime lesson09AdvisorRuntime(
            ChatClient.Builder builder,
            Lesson09LoggingAdvisor loggingAdvisor,
            Lesson09UserContextAdvisor userContextAdvisor) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();

        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor
                .builder(chatMemory)
                .build();

        ChatClient chatClient = builder
                .clone()
                .defaultAdvisors(
                        loggingAdvisor,
                        userContextAdvisor,
                        memoryAdvisor
                )
                .build();

        return new Lesson09AdvisorRuntime(chatClient, chatMemory);
    }
}
