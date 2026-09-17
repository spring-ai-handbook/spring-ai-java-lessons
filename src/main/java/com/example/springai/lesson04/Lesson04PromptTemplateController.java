package com.example.springai.lesson04;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson04：PromptTemplate 对外测试接口。
 */
@RestController
@RequestMapping("/lesson04")
@RequiredArgsConstructor
public class Lesson04PromptTemplateController {

    /**
     * PromptTemplate 学习服务。
     */
    private final Lesson04PromptTemplateService promptTemplateService;

    /**
     * 根据动态参数生成学习计划。
     *
     * @param request 学习计划请求
     * @return 动态 Prompt 生成结果
     */
    @PostMapping("/plan")
    public String createPlan(@Valid @RequestBody Lesson04StudyPlanRequest request) {
        return promptTemplateService.createPlan(request);
    }
}
