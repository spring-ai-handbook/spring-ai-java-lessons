package com.example.aidemo1.lesson05;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Lesson05：Structured Output 测试接口。
 */
@RestController
@RequestMapping("/lesson05")
@RequiredArgsConstructor
public class Lesson05StructuredOutputController {

    /**
     * Structured Output 学习服务。
     */
    private final Lesson05StructuredOutputService structuredOutputService;

    /**
     * 测试单个 DTO 结构化输出。
     *
     * @param topic 学习主题
     * @return 单本图书
     */
    @GetMapping("/book")
    public Lesson05BookInfo recommendOne(@RequestParam String topic) {
        return structuredOutputService.recommendOne(topic);
    }

    /**
     * 测试泛型 List 结构化输出。
     *
     * @param topic 学习主题
     * @return 图书列表
     */
    @GetMapping("/books")
    public List<Lesson05BookInfo> recommendList(@RequestParam String topic) {
        return structuredOutputService.recommendList(topic);
    }
}
