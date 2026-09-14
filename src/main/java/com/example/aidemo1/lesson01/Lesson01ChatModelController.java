package com.example.aidemo1.lesson01;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson01：ChatModel 对外测试接口。
 */
@RestController
@RequestMapping("/lesson01")
@RequiredArgsConstructor
public class Lesson01ChatModelController {

    /**
     * ChatModel 学习服务。
     */
    private final Lesson01ChatModelService chatModelService;

    /**
     * 最基础的模型聊天接口。
     *
     * @param message 用户输入
     * @return 模型回答
     */
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatModelService.chat(message);
    }
}
