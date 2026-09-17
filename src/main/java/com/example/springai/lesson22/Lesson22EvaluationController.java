package com.example.springai.lesson22;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Lesson22 Evaluation 最小示例。
 *
 * <p>本课使用 {@link RelevancyEvaluator}，让一个 ChatModel 充当“评委”，
 * 判断给定回答是否与用户问题和上下文相关。</p>
 */
@RestController
@RequestMapping("/lesson22")
@RequiredArgsConstructor
public class Lesson22EvaluationController {

    /** RelevancyEvaluator 内部同样需要 ChatClient 来执行评估 Prompt。 */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 评估一个回答与问题、上下文是否相关。
     *
     * @param request 问题、上下文和待评估回答
     * @return 是否通过、得分和反馈
     */
    @PostMapping("/relevancy")
    public EvaluationResult relevancy(@RequestBody EvaluationInput request) {
        validate(request);

        Document contextDocument = Document.builder()
            .text(request.context())
            .build();

        EvaluationRequest evaluationRequest = new EvaluationRequest(
            request.question(),
            List.of(contextDocument),
            request.answer()
        );

        RelevancyEvaluator evaluator = new RelevancyEvaluator(chatClientBuilder);
        EvaluationResponse response = evaluator.evaluate(evaluationRequest);

        return new EvaluationResult(
            response.isPass(),
            response.getScore(),
            response.getFeedback()
        );
    }

    /** 基础参数校验。 */
    private void validate(EvaluationInput request) {
        if (request == null
            || !StringUtils.hasText(request.question())
            || !StringUtils.hasText(request.context())
            || !StringUtils.hasText(request.answer())) {
            throw new IllegalArgumentException("question、context、answer 都不能为空");
        }
    }

    /** Evaluation 输入。 */
    public record EvaluationInput(String question, String context, String answer) {
    }

    /** Evaluation 结果。 */
    public record EvaluationResult(boolean pass, float score, String feedback) {
    }
}
