package com.example.aidemo1.lesson09;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * Lesson09：Advisor 综合调用服务。
 */
@Service
@RequiredArgsConstructor
public class Lesson09AdvisorService {

    /**
     * Lesson09 专用 ChatClient / ChatMemory 运行时。
     */
    private final Lesson09AdvisorRuntime runtime;

    /**
     * 普通同步聊天。
     *
     * @param conversationId 会话 ID
     * @param userId 模拟当前登录用户 ID
     * @param message 用户消息
     * @return AI 回答
     */
    public String chat(
            String conversationId,
            Long userId,
            String message) {
        return runtime
                .chatClient()
                .prompt()
                .user(message)
                .advisors(advisor -> advisor
                        .param(ChatMemory.CONVERSATION_ID, conversationId)
                        .param(Lesson09UserContextAdvisor.USER_ID, userId))
                .call()
                .content();
    }

    /**
     * 流式聊天。
     *
     * @param conversationId 会话 ID
     * @param userId 模拟当前登录用户 ID
     * @param message 用户消息
     * @return AI 文本流
     */
    public Flux<String> stream(
            String conversationId,
            Long userId,
            String message) {
        return runtime
                .chatClient()
                .prompt()
                .user(message)
                .advisors(advisor -> advisor
                        .param(ChatMemory.CONVERSATION_ID, conversationId)
                        .param(Lesson09UserContextAdvisor.USER_ID, userId))
                .stream()
                .content();
    }

    /**
     * 清空 Lesson09 指定会话的 Chat Memory。
     *
     * @param conversationId 会话 ID
     * @return 清理结果
     */
    public Map<String, Object> clearMemory(String conversationId) {
        runtime.chatMemory().clear(conversationId);
        return Map.of(
                "conversationId", conversationId,
                "cleared", true
        );
    }
}
