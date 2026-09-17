package com.example.springai.lesson04;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * Lesson04：PromptTemplate 动态参数学习服务。
 */
@Service
@RequiredArgsConstructor
public class Lesson04PromptTemplateService {

    /**
     * Spring Boot 自动配置的 ChatClient.Builder。
     */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 通过 Prompt 模板变量生成个性化学习计划。
     *
     * @param request 学习计划参数
     * @return 模型生成的学习计划
     */
    public String createPlan(Lesson04StudyPlanRequest request) {
        ChatClient chatClient = chatClientBuilder.clone().build();

        return chatClient
                .prompt()
                .system("你是一名资深 Java 技术导师，负责制定务实、循序渐进的学习计划。")
                .user(user -> user
                        .text("""
                                我正在学习 {technology}。
                                我有 {years} 年 Java 开发经验。
                                请为我制定一份 {days} 天的学习计划。

                                要求：
                                1. 每天明确学习目标。
                                2. 每天给出一个可实践的小任务。
                                3. 最后给出验收标准。
                                """)
                        .param("technology", request.technology())
                        .param("years", request.years())
                        .param("days", request.days()))
                .call()
                .content();
    }
}
