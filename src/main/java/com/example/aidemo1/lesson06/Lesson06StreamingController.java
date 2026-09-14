package com.example.aidemo1.lesson06;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Lesson06：Streaming / SSE 测试接口。
 */
@RestController
@RequestMapping("/lesson06")
@RequiredArgsConstructor
public class Lesson06StreamingController {

    /**
     * Streaming 学习服务。
     */
    private final Lesson06StreamingService streamingService;

    /**
     * 通过 SSE 边生成边返回大模型内容。
     *
     * @param message 用户消息
     * @return SSE 文本流
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String message) {
        return streamingService.stream(message);
    }
}
