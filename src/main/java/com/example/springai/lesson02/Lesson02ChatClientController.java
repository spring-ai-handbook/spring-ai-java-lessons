package com.example.springai.lesson02;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson02：ChatClient 测试接口。
 */
@RestController
@RequestMapping("/lesson02")
@RequiredArgsConstructor
public class Lesson02ChatClientController {

    /**
     * ChatClient 学习服务。
     */
    private final Lesson02ChatClientService chatClientService;

    /**
     * 使用 ChatClient 调用大模型。
     *
     * @param message 用户输入
     * @return 模型回答
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClientService.chat(message);
    }
}
