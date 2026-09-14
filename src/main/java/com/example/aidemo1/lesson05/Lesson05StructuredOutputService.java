package com.example.aidemo1.lesson05;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lesson05：Structured Output 学习服务。
 *
 * <p>演示把大模型生成的自然语言结果直接转换为 Java DTO，
 * 避免业务层长期依赖手工解析字符串。</p>
 */
@Service
@RequiredArgsConstructor
public class Lesson05StructuredOutputService {

    /**
     * Spring Boot 自动配置的 ChatClient.Builder。
     */
    private final ChatClient.Builder chatClientBuilder;

    /**
     * 获取单个结构化对象。
     *
     * @param topic 技术主题
     * @return 结构化图书信息
     */
    public Lesson05BookInfo recommendOne(String topic) {
        ChatClient chatClient = chatClientBuilder.clone().build();

        return chatClient
                .prompt()
                .system("你是一名技术图书推荐顾问，只推荐真实存在且适合学习的书籍。")
                .user("请为学习 " + topic + " 推荐一本技术书，并说明作者、难度和推荐理由。")
                .call()
                .entity(Lesson05BookInfo.class);
    }

    /**
     * 获取 List 形式的结构化输出。
     *
     * @param topic 技术主题
     * @return 三本结构化图书信息
     */
    public List<Lesson05BookInfo> recommendList(String topic) {
        ChatClient chatClient = chatClientBuilder.clone().build();

        return chatClient
                .prompt()
                .system("你是一名技术图书推荐顾问，只推荐真实存在且适合学习的书籍。")
                .user("请为学习 " + topic + " 推荐三本技术书。")
                .call()
                .entity(new ParameterizedTypeReference<List<Lesson05BookInfo>>() {
                });
    }
}
