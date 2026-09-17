package com.example.springai.lesson07;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lesson07：Chat Memory 多轮对话学习服务。
 */
@Service
@RequiredArgsConstructor
public class Lesson07ChatMemoryService {

    /**
     * Spring Boot 自动配置的 ChatClient.Builder。
     */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * Lesson07 配置的滑动窗口 ChatMemory。
     */
    private final ChatMemory lesson07ChatMemory;

    /**
     * 使用 conversationId 建立独立多轮会话。
     *
     * @param conversationId 会话唯一标识
     * @param message 用户当前消息
     * @return 模型回答
     */
    public String chat(String conversationId, String message) {
        ChatClient chatClient = createChatClient();

        return chatClient
                .prompt()
                .user(message)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId
                ))
                .call()
                .content();
    }

    /**
     * 查询指定 conversationId 当前仍保留在 ChatMemory 中的消息。
     *
     * @param conversationId 会话唯一标识
     * @return 当前记忆窗口内的消息
     */
    public List<Lesson07MemoryMessage> getMemory(String conversationId) {
        return lesson07ChatMemory
                .get(conversationId)
                .stream()
                .map(message -> new Lesson07MemoryMessage(
                        message.getMessageType().name(),
                        message.getText()
                ))
                .toList();
    }

    /**
     * 清空指定会话的 Chat Memory。
     *
     * @param conversationId 会话唯一标识
     */
    public void clear(String conversationId) {
        lesson07ChatMemory.clear(conversationId);
    }

    /**
     * 创建仅属于 Lesson07 的 ChatClient，并安装 MessageChatMemoryAdvisor。
     *
     * @return 配置好 Chat Memory Advisor 的 ChatClient
     */
    private ChatClient createChatClient() {
        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor
                .builder(lesson07ChatMemory)
                .build();

        return chatClientBuilder
                .clone()
                .defaultAdvisors(memoryAdvisor)
                .build();
    }
}
