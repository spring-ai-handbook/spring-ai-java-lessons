package com.example.springai.lesson18;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson18 ImageModel 最小示例。
 *
 * <p>默认课程配置关闭图片模型，因此只有项目中真实存在
 * {@link ImageModel} Bean 时，本 Controller 才会被创建。</p>
 */
@RestController
@RequestMapping("/lesson18")
@RequiredArgsConstructor
@ConditionalOnBean(ImageModel.class)
public class Lesson18ImageModelController {

    /** Spring AI 统一图片生成模型接口。 */
    private final ImageModel imageModel;

    /**
     * 根据文字生成图片。
     *
     * @param prompt 图片描述
     * @return 图片 URL 或 Base64 数据
     */
    @GetMapping("/generate")
    public ImageResult generate(@RequestParam String prompt) {
        if (!StringUtils.hasText(prompt)) {
            throw new IllegalArgumentException("prompt 不能为空");
        }

        ImageResponse response = imageModel.call(new ImagePrompt(prompt));
        ImageGeneration generation = response.getResult();
        if (generation == null) {
            throw new IllegalStateException("图片模型没有返回生成结果");
        }

        Image image = generation.getOutput();
        return new ImageResult(image.getUrl(), image.getB64Json());
    }

    /** 图片生成结果。 */
    public record ImageResult(String url, String b64Json) {
    }
}
