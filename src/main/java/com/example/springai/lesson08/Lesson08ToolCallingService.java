package com.example.springai.lesson08;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Lesson08：Tool Calling 聊天服务。
 */
@Service
@RequiredArgsConstructor
public class Lesson08ToolCallingService {

    /**
     * Spring Boot 自动配置的 ChatClient.Builder。
     */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 提供给模型选择调用的订单 Tool。
     */
    private final Lesson08OrderTool orderTool;

    /**
     * 进行一次支持 Tool Calling 的订单客服聊天。
     *
     * @param userId 模拟的当前登录用户 ID；真实项目应来自 JWT / Spring Security
     * @param message 用户自然语言问题
     * @return 模型最终回答或 returnDirect Tool 的直接结果
     */
    public String chat(Long userId, String message) {
        ChatClient chatClient = chatClientBuilder.clone().build();

        return chatClient
                .prompt()
                .system("""
                        你是一名订单客服助手。
                        当用户询问订单、物流、订单列表等真实业务数据时，必须优先调用提供的订单 Tool，
                        不允许凭空编造订单状态。
                        """)
                .user(message)
                .tools(orderTool)
                .toolContext(Map.of(Lesson08OrderTool.USER_ID, userId))
                .call()
                .content();
    }
}
