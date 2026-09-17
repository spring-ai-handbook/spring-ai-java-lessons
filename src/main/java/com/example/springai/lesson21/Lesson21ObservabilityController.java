package com.example.springai.lesson21;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson21 Observability 最小示例。
 *
 * <p>Spring AI 的 ChatClient、ChatModel、EmbeddingModel、VectorStore 等核心组件
 * 已经集成 Micrometer Observation。本 Controller 只负责产生一次真实 AI 调用，
 * 然后通过 Spring Boot Actuator 查看调用耗时、次数和 Token 等指标。</p>
 */
@RestController
@RequestMapping("/lesson21")
@RequiredArgsConstructor
public class Lesson21ObservabilityController {

    /** Spring Boot 自动配置的 ChatClient Builder，底层仍使用 DeepSeek。 */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 发起一次普通 AI 请求，用来产生 Spring AI Observation / Metrics。
     *
     * @param message 用户消息
     * @return AI 回答
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        if (!StringUtils.hasText(message)) {
            throw new IllegalArgumentException("message 不能为空");
        }

        return chatClientBuilder
            .build()
            .prompt()
            .user(message)
            .call()
            .content();
    }
}
