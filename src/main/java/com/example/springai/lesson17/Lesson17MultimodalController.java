package com.example.springai.lesson17;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson17 多模态最小示例。
 *
 * <p>演示如何通过 Spring AI：</p>
 *
 * <pre>
 * 文本
 * +
 * 图片
 * ↓
 * ChatModel
 * </pre>
 *
 * <p>注意：
 * 当前项目主模型 DeepSeek 在 Spring AI 2.0.1 官方能力表中
 * 主要是文本模型，因此本代码用于快速理解多模态 API。
 * 真正运行时需要换成支持图片输入的 ChatModel。</p>
 */
@RestController
@RequestMapping("/lesson17")
@RequiredArgsConstructor
public class Lesson17MultimodalController {

    /**
     * Spring AI ChatModel。
     *
     * <p>实际是否可以处理图片，
     * 取决于底层模型本身是否支持多模态。</p>
     */
    private final ChatModel chatModel;

    /**
     * 使用文本 + 图片进行多模态问答。
     *
     * @return 模型对图片的描述
     */
    @GetMapping("/image")
    public String image() {

        return ChatClient.create(chatModel)
            .prompt()
            .user(user -> user

                /*
                 * 普通文本提示词。
                 */
                .text("请用中文告诉我这张图片里有什么")

                /*
                 * media 表示附加媒体内容。
                 *
                 * 第一个参数：
                 * 图片 MIME 类型。
                 *
                 * 第二个参数：
                 * Spring Resource。
                 */
                .media(
                    MimeTypeUtils.IMAGE_PNG,
                    new ClassPathResource(
                        "images/test.png"
                    )
                )
            )
            .call()
            .content();
    }
}
