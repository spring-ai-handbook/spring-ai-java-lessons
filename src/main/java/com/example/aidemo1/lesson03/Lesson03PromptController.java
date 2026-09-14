package com.example.aidemo1.lesson03;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson03：System Prompt / User Prompt 测试接口。
 */
@RestController
@RequestMapping("/lesson03")
@RequiredArgsConstructor
public class Lesson03PromptController {

    /**
     * Prompt 学习服务。
     */
    private final Lesson03PromptService promptService;

    /**
     * 测试 System Prompt 与 User Prompt 的职责差异。
     *
     * @param message 用户问题
     * @return 模型回答
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return promptService.chat(message);
    }
}
