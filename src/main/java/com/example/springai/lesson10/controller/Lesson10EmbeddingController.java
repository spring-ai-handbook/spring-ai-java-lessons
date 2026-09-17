package com.example.springai.lesson10.controller;

import com.example.springai.lesson10.dto.Lesson10EmbeddingResponse;
import com.example.springai.lesson10.dto.Lesson10SimilarityRequest;
import com.example.springai.lesson10.dto.Lesson10SimilarityResponse;
import com.example.springai.lesson10.service.Lesson10EmbeddingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/lesson10")
@RequiredArgsConstructor
public class Lesson10EmbeddingController {

    /**
     * Lesson10 Embedding 业务服务。
     */
    private final Lesson10EmbeddingService embeddingService;


    @GetMapping("/embedding")
    public Lesson10EmbeddingResponse embedding(
        @RequestParam
        @NotBlank(message = "text 不能为空")
        String text) {

        return embeddingService.embed(text);
    }

    /**
     * 比较两段文本之间的语义相似度。
     *
     * <p>
     * 两段文本首先会被转换成 Embedding 向量，
     * 然后通过余弦相似度进行比较。
     * </p>
     *
     * @param request 文本相似度比较请求
     * @return 相似度比较结果
     */
    @PostMapping("/similarity")
    public Lesson10SimilarityResponse similarity(
        @Valid
        @RequestBody
        Lesson10SimilarityRequest request) {

        return embeddingService.similarity(request);
    }

}
