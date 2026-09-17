package com.example.springai.lesson01;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

/**
 * Lesson01：ChatModel 基础调用服务。
 *
 * <p>{@link ChatModel} 是 Spring AI 对聊天模型的统一抽象。
 * 当前项目底层实际注入的是 DeepSeekChatModel，但业务代码只依赖 ChatModel 接口。</p>
 */
@Service
@RequiredArgsConstructor
public class Lesson01ChatModelService {

    /**
     * Spring AI 统一聊天模型接口。
     */
    private final ChatModel chatModel;

    /**
     * 使用最基础的 {@link ChatModel#call(String)} 调用大模型。
     *
     * @param message 用户消息
     * @return 模型生成的文本
     */
    public String chat(String message) {
        return chatModel.call(message);
    }
}
