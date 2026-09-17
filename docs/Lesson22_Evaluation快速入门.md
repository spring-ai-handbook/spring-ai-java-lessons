# Lesson22：Evaluation / AI 回答评估快速入门

> 目标：知道“模型生成答案之后，还可以再用 Evaluator 判断答案质量”。只学习 `RelevancyEvaluator`。

## 1. 为什么需要 Evaluation

RAG 能回答不代表回答一定正确。

```text
用户问题
↓
RAG
↓
模型回答
```

后面可以再加：

```text
问题 + Context + 模型回答
↓
Evaluator
↓
Pass / Fail
```

所以：

```text
Generation
负责生成

Evaluation
负责评价
```

## 2. 本课只认识 RelevancyEvaluator

Spring AI 2.0.1 提供：

```java
RelevancyEvaluator
```

它判断：

```text
这个回答
是否与用户问题 + 给定上下文相关
```

核心输入：

```text
userText
context Documents
responseContent
```

对应：

```java
EvaluationRequest
```

输出：

```java
EvaluationResponse
```

最重要字段：

```java
isPass()
getScore()
getFeedback()
```

## 3. 最核心代码

```java
Document contextDocument = Document.builder()
    .text(context)
    .build();

EvaluationRequest request = new EvaluationRequest(
    question,
    List.of(contextDocument),
    answer
);

RelevancyEvaluator evaluator =
    new RelevancyEvaluator(chatClientBuilder);

EvaluationResponse response =
    evaluator.evaluate(request);
```

然后：

```java
response.isPass();
response.getScore();
response.getFeedback();
```

## 4. 注意：Evaluator 也会调用大模型

这点必须理解。

```text
原模型生成答案
↓
Evaluator
↓
再次调用 ChatModel
↓
让模型充当“评委”
```

所以 Evaluation：

```text
不是免费的
也不是普通 Java if 判断
```

它会增加：

```text
Token
耗时
调用成本
```

## 5. 测试接口

```http
POST http://localhost:8080/lesson22/relevancy
Content-Type: application/json

{
  "question": "退款期限是多少天？",
  "context": "普通商品签收后7天内可以申请无理由退货。",
  "answer": "普通商品签收后7天内可以申请无理由退货。"
}
```

预期：

```text
pass = true
```

再测试一个明显错误回答：

```json
{
  "question": "退款期限是多少天？",
  "context": "普通商品签收后7天内可以申请无理由退货。",
  "answer": "退款期限是30天。"
}
```

观察 `pass / score / feedback`。

## 6. Relevancy 和 Fact Checking 区别

快速知道即可：

```text
RelevancyEvaluator
→ 回答和问题、Context 是否相关

FactCheckingEvaluator
→ 回答中的事实是否被 Context 支持
```

当前不要继续深挖。

## 7. 必须会回答

```text
Evaluation 是什么？
→ 对 AI 生成结果再做质量评估。

RelevancyEvaluator 做什么？
→ 判断回答是否与问题和 Context 相关。

Evaluator 会不会调用模型？
→ 会。
```

## 8. 官方参考

- https://docs.spring.io/spring-ai/reference/api/testing.html
- https://docs.spring.io/spring-ai/docs/current/api/org/springframework/ai/chat/evaluation/RelevancyEvaluator.html
