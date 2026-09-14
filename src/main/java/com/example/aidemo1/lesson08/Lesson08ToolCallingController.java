package com.example.aidemo1.lesson08;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson08：Tool Calling 测试接口。
 */
@RestController
@RequestMapping("/lesson08")
@RequiredArgsConstructor
public class Lesson08ToolCallingController {

    /**
     * Tool Calling 学习服务。
     */
    private final Lesson08ToolCallingService toolCallingService;

    /**
     * 支持订单查询 Tool 的自然语言聊天接口。
     *
     * <p>这里通过请求参数传 userId 只用于课程实验。
     * 真实项目必须从 JWT / Spring Security 获取当前登录用户。</p>
     *
     * @param userId 模拟当前登录用户 ID
     * @param message 用户问题
     * @return AI 最终回答
     */
    /**
     * 发起一次带 Tool Calling 能力的同步对话。
     *
     * @param userId 当前登录用户 ID（课程演示参数）
     * @param message 用户输入
     * @return 模型最终回答
     */
    @GetMapping("/chat")
    public String chat(
            @RequestParam Long userId,
            @RequestParam String message) {
        return toolCallingService.chat(userId, message);
    }
}
