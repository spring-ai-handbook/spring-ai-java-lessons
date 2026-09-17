package com.example.springai.lesson20;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.moderation.Categories;
import org.springframework.ai.moderation.CategoryScores;
import org.springframework.ai.moderation.Generation;
import org.springframework.ai.moderation.Moderation;
import org.springframework.ai.moderation.ModerationModel;
import org.springframework.ai.moderation.ModerationPrompt;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.ai.moderation.ModerationResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Lesson20 Spring AI 内容审核最小示例。
 *
 * <p>完整流程：</p>
 *
 * <pre>
 * String
 * ↓
 * ModerationPrompt
 * ↓
 * ModerationModel
 * ↓
 * ModerationResponse
 * ↓
 * ModerationResult
 * </pre>
 */
@RestController
@RequestMapping("/lesson20")
@RequiredArgsConstructor
@ConditionalOnBean(ModerationModel.class)
public class Lesson20ModerationController {

    /**
     * Spring AI 内容审核统一接口。
     *
     * <p>前提：</p>
     *
     * <pre>
     * spring.ai.model.moderation=openai
     * </pre>
     *
     * <p>否则不会创建这个 Bean。</p>
     */
    private final ModerationModel moderationModel;

    /**
     * 审核用户输入文本。
     *
     * @param text 要审核的文字
     * @return 审核结果
     */
    @GetMapping("/check")
    public ModerationCheckResult check(
        @RequestParam String text) {

        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(
                "text 不能为空"
            );
        }

        /*
         * 第一步：
         * 普通 String 包装成 ModerationPrompt。
         */
        ModerationPrompt prompt =
            new ModerationPrompt(text);

        /*
         * 第二步：
         * 调用 ModerationModel。
         *
         * 当前底层使用 OpenAI Moderation。
         */
        ModerationResponse response =
            moderationModel.call(prompt);

        /*
         * 第三步：
         * 获取第一条 Generation。
         */
        Generation generation =
            response.getResult();

        if (generation == null) {
            throw new IllegalStateException(
                "Moderation 没有返回结果"
            );
        }

        /*
         * Generation 的输出是 Moderation。
         */
        Moderation moderation =
            generation.getOutput();

        /*
         * 一个 Moderation 中包含审核结果列表。
         */
        List<ModerationResult> results =
            moderation.getResults();

        if (results == null || results.isEmpty()) {
            throw new IllegalStateException(
                "Moderation 结果为空"
            );
        }

        ModerationResult result =
            results.get(0);

        /*
         * flagged：
         *
         * true
         * → 内容被模型判定为命中某种风险类别
         *
         * false
         * → 没有被标记
         */
        return new ModerationCheckResult(
            result.isFlagged(),
            result.getCategories(),
            result.getCategoryScores()
        );
    }

    /**
     * 内容审核接口返回结果。
     *
     * @param flagged       是否被标记
     * @param categories    风险类别判断
     * @param categoryScores 各风险类别对应的分数
     */
    public record ModerationCheckResult(
        boolean flagged,
        Categories categories,
        CategoryScores categoryScores
    ) {
    }
}
