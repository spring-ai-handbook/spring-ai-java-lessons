package com.example.aidemo1.lesson09;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * Lesson09：Advisor 对外测试接口。
 *
 * <p>同时提供普通调用和 SSE Streaming 调用，
 * 用于观察 LoggingAdvisor、UserContextAdvisor、MemoryAdvisor 的调用链。</p>
 */
@RestController
@RequestMapping("/lesson09")
@RequiredArgsConstructor
public class Lesson09AdvisorController {

    /**
     * Advisor 综合学习服务。
     */
    private final Lesson09AdvisorService advisorService;

    /**
     * 普通 Advisor 调用接口。
     *
     * @param conversationId 会话 ID
     * @param userId 模拟当前登录用户 ID
     * @param message 用户消息
     * @return AI 回答
     */
    /**
     * 普通同步 Advisor 调用测试。
     *
     * @param conversationId 会话 ID
     * @param userId 当前用户 ID（课程演示参数）
     * @param message 用户输入
     * @return 模型回答
     */
    @GetMapping("/chat")
    public String chat(
            @RequestParam String conversationId,
            @RequestParam Long userId,
            @RequestParam String message) {
        return advisorService.chat(conversationId, userId, message);
    }

    /**
     * Streaming Advisor 调用接口。
     *
     * @param conversationId 会话 ID
     * @param userId 模拟当前登录用户 ID
     * @param message 用户消息
     * @return SSE 文本流
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(
            @RequestParam String conversationId,
            @RequestParam Long userId,
            @RequestParam String message) {
        return advisorService.stream(conversationId, userId, message);
    }

    /**
     * 清空当前会话 Memory。
     *
     * @param conversationId 会话 ID
     * @return 清理结果
     */
    @DeleteMapping("/memory")
    public Map<String, Object> clearMemory(@RequestParam String conversationId) {
        return advisorService.clearMemory(conversationId);
    }
}
