# Lesson20：Moderation / 内容审核快速入门

> Spring Boot 4.1.1 / Spring AI 2.0.1 / JDK 21  
> 目标：只认识 Spring AI 内容审核的核心链路，不做复杂风控系统。

## 1. 核心链路

```text
String
↓
ModerationPrompt
↓
ModerationModel
↓
ModerationResponse
↓
ModerationResult
↓
isFlagged()
```

一句话：

```text
ModerationModel = Spring AI 对内容审核模型的统一抽象。
```

## 2. 为什么默认项目里不直接开启

完整课程项目默认配置：

```yaml
spring:
  ai:
    model:
      moderation: none
```

这样做是为了让主项目在没有 OpenAI Moderation Key 时也能正常启动。

Lesson20 Controller 使用：

```java
@ConditionalOnBean(ModerationModel.class)
```

所以：

```text
没有 ModerationModel
→ Lesson20 Controller 不加载
→ 不影响其他课程
```

如果你要实际运行 Lesson20，再临时开启：

```yaml
spring:
  ai:
    model:
      moderation: openai

    openai:
      api-key: ${OPENAI_API_KEY}
```

## 3. 最核心代码

```java
ModerationResponse response =
    moderationModel.call(
        new ModerationPrompt(text)
    );

ModerationResult result =
    response
        .getResult()
        .getOutput()
        .getResults()
        .get(0);

boolean flagged = result.isFlagged();
```

重点只认识：

```text
flagged
→ 是否被整体标记

categories
→ 命中了哪些风险类别

categoryScores
→ 各类别对应的模型分数
```

## 4. 测试接口

项目代码：

```text
src/main/java/com/example/springai/lesson20/
Lesson20ModerationController.java
```

接口：

```http
GET http://localhost:8080/lesson20/check?text=你好，我正在学习Spring AI
```

注意：只有开启 `moderation: openai` 后该接口才会存在。

## 5. 它和 ChatModel 有什么关系

没有直接关系。

```text
用户文本
↓
ModerationModel
↓
审核结果
```

DeepSeek 不参与。

真实聊天系统可以这样组合：

```text
用户输入
↓
Moderation
↓
通过
↓
DeepSeek
↓
回答
```

也可以审核输出：

```text
DeepSeek 回答
↓
Moderation
↓
返回用户
```

## 6. 快速学习阶段到这里就够了

不用继续深入：

```text
复杂阈值
人工复核
风控规则引擎
审核日志平台
输入输出双重审核架构
```

### 必须会回答

```text
ModerationModel 是什么？
→ 内容审核模型统一接口。

最重要结果？
→ ModerationResult.isFlagged()。

moderation: none 会怎样？
→ 不创建 ModerationModel Bean。
```

## 7. 官方参考

- https://docs.spring.io/spring-ai/reference/api/moderation.html
- https://docs.spring.io/spring-ai/reference/api/moderation/openai-moderation.html
