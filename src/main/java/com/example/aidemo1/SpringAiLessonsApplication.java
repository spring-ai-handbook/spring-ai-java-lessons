package com.example.aidemo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring AI Lesson01～Lesson09 累积学习项目启动类。
 *
 * <p>每一课使用独立的 lessonXX 包保存代码，后续课程不会覆盖前面课程，
 * 方便随时回顾每个阶段学习过的 API 与设计思想。</p>
 */
@SpringBootApplication
public class SpringAiLessonsApplication {

    /**
     * Spring Boot 应用启动入口。
     *
     * @param args JVM 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringAiLessonsApplication.class, args);
    }
}
