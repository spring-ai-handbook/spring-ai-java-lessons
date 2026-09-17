package com.example.springai.lesson07;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Lesson07：Chat Memory 测试接口。
 */
@RestController
@RequestMapping("/lesson07")
@RequiredArgsConstructor
public class Lesson07ChatMemoryController {

    /**
     * Chat Memory 学习服务。
     */
    private final Lesson07ChatMemoryService chatMemoryService;

    /**
     * 多轮聊天接口。
     *
     * @param conversationId 会话 ID
     * @param message 用户消息
     * @return 模型回答
     */
    @GetMapping("/chat")
    public String chat(
            @RequestParam String conversationId,
            @RequestParam String message) {
        return chatMemoryService.chat(conversationId, message);
    }

    /**
     * 查看当前 Chat Memory 保存的消息。
     *
     * @param conversationId 会话 ID
     * @return 当前滑动窗口内的消息
     */
    @GetMapping("/memory")
    public List<Lesson07MemoryMessage> memory(@RequestParam String conversationId) {
        return chatMemoryService.getMemory(conversationId);
    }

    /**
     * 清空指定会话记忆。
     *
     * @param conversationId 会话 ID
     * @return 清理结果
     */
    @DeleteMapping("/memory")
    public Map<String, Object> clear(@RequestParam String conversationId) {
        chatMemoryService.clear(conversationId);
        return Map.of(
                "conversationId", conversationId,
                "cleared", true
        );
    }
}
