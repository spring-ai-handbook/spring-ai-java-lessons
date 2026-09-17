# Lesson21：Observability / AI 调用监控快速入门

> 目标：知道 Spring AI 调用如何被 Micrometer / Actuator 观察，能看到调用次数、耗时和 Token 指标即可。

## 1. Observability 是什么

你以前调用：

```text
ChatClient
↓
ChatModel
↓
DeepSeek
```

现在只是多了一层“观察”：

```text
ChatClient
↓
Micrometer Observation
↓
ChatModel
↓
DeepSeek
```

Spring AI 2.0.1 已经为核心组件提供 Observation，包括：

```text
ChatClient
ChatModel
EmbeddingModel
ImageModel
VectorStore
```

所以这课不是自己写计时器。

## 2. 新增依赖

项目 `pom.xml` 已加入：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

`application.yml` 已加入：

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics
```

## 3. Lesson21 代码为什么这么简单

```java
@GetMapping("/chat")
public String chat(@RequestParam String message) {
    return chatClientBuilder
        .build()
        .prompt()
        .user(message)
        .call()
        .content();
}
```

因为 Spring AI 自己会记录 Observation。

这个接口只是为了产生一次真实 AI 请求。

## 4. 测试

先调用：

```http
GET http://localhost:8080/lesson21/chat?message=用一句话解释Spring AI
```

然后查看所有 metrics：

```http
GET http://localhost:8080/actuator/metrics
```

Spring AI 常见指标包括：

```text
gen.ai.chat.client.operation
gen.ai.client.operation
gen.ai.client.token.usage
```

其中可以理解为：

```text
gen.ai.chat.client.operation
→ ChatClient 调用耗时 / 次数

gen.ai.client.operation
→ 模型真正执行调用的耗时 / 次数

gen.ai.client.token.usage
→ 输入 / 输出 / 总 Token 使用量
```

## 5. Prompt 和 Completion 默认不会记录

Spring AI 默认不会把完整 Prompt / Completion 放进日志，因为可能包含隐私数据。

如果排错时临时需要，可以开启：

```yaml
spring:
  ai:
    chat:
      observations:
        log-prompt: true
        log-completion: true
```

学习时知道即可。

正式项目不要随便开启，因为可能把：

```text
用户隐私
企业数据
Prompt
模型回答
```

写进日志。

## 6. Metrics 和 Tracing 区别

快速理解：

```text
Metrics
→ 统计数字
→ 调用了多少次、平均多久、Token多少

Tracing
→ 一次请求完整链路
→ Controller → ChatClient → Model → HTTP
```

当前只掌握 Metrics 就够了。

## 7. 必须会回答

```text
Observability 是什么？
→ 观察 AI 调用的耗时、次数、Token、Trace 等信息。

是不是每个接口自己写 System.currentTimeMillis()？
→ 不是，Spring AI 已经集成 Micrometer Observation。

怎么快速查看？
→ Spring Boot Actuator /actuator/metrics。
```

## 8. 官方参考

- https://docs.spring.io/spring-ai/reference/observability/
