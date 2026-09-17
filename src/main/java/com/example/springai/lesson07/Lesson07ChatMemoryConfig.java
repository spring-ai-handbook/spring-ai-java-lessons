package com.example.springai.lesson07;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Lesson07：Chat Memory 配置。
 */
@Configuration
public class Lesson07ChatMemoryConfig {

    /**
     * 创建内存版滑动窗口 ChatMemory。
     *
     * <p>最多保留 10 条消息，超过窗口后旧消息会被淘汰，
     * 用于观察短期上下文记忆的工作方式。</p>
     *
     * @return ChatMemory 实例
     */
    /**
     * 创建 Lesson07 使用的滑动窗口聊天记忆。
     *
     * @return 最多保留 10 条消息的 ChatMemory
     */
    @Bean
    public ChatMemory lesson07ChatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();
    }
}
