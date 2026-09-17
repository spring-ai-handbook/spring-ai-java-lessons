package com.example.springai.lesson23;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson23 Model Options / Metadata / Usage 最小示例。
 *
 * <p>这一课把 Spring AI 基础阶段最后几个常用点放在一起：</p>
 *
 * <pre>
 * Runtime ChatOptions
 * ↓
 * ChatResponse
 * ↓
 * ChatResponseMetadata
 * ↓
 * Usage / model / responseId / finishReason
 * </pre>
 */
@RestController
@RequestMapping("/lesson23")
@RequiredArgsConstructor
public class Lesson23ModelMetadataController {

    /** Spring Boot 自动配置的 ChatClient Builder。 */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 使用请求级 ChatOptions 调用模型，并读取完整 Response Metadata。
     *
     * @param request 用户消息与可选生成参数
     * @return 内容、模型信息、Token Usage 和结束原因
     */
    @PostMapping("/chat")
    public ChatMetadataResult chat(@RequestBody ChatMetadataRequest request) {
        if (request == null || !StringUtils.hasText(request.message())) {
            throw new IllegalArgumentException("message 不能为空");
        }

        ChatOptions.Builder<?> options = ChatOptions.builder()
            .maxTokens(request.maxTokens())
            .temperature(request.temperature());

        ChatResponse response = chatClientBuilder
            .build()
            .prompt()
            .user(request.message())
            .options(options)
            .call()
            .chatResponse();

        if (response == null || response.getResult() == null) {
            throw new IllegalStateException("模型没有返回 ChatResponse");
        }

        ChatResponseMetadata metadata = response.getMetadata();
        Usage usage = metadata.getUsage();

        return new ChatMetadataResult(
            response.getResult().getOutput().getText(),
            metadata.getId(),
            metadata.getModel(),
            usage.getPromptTokens(),
            usage.getCompletionTokens(),
            usage.getTotalTokens(),
            response.getResult().getMetadata().getFinishReason()
        );
    }

    /**
     * 请求级模型参数。
     *
     * <p>temperature 和 maxTokens 都是常见的可移植 ChatOptions。
     * 某些“思考模式”模型可能忽略 temperature，最终以模型供应商能力为准。</p>
     */
    public record ChatMetadataRequest(
        String message,
        Double temperature,
        Integer maxTokens
    ) {
    }

    /** ChatResponse 中最值得快速掌握的元数据。 */
    public record ChatMetadataResult(
        String content,
        String responseId,
        String model,
        Integer promptTokens,
        Integer completionTokens,
        Integer totalTokens,
        String finishReason
    ) {
    }
}
