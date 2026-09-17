# Lesson23：Model Options / Metadata / Usage + Spring AI 基础收尾

> 这是 Spring AI 基础阶段最后一课。  
> 目标：会设置请求级模型参数，会读取 `ChatResponse` 的 Token / Model / FinishReason，然后把整个 Spring AI 知识地图收束起来。

---

# 一、为什么最后学这个

以前大部分代码直接：

```java
chatClient
    .prompt()
    .user(message)
    .call()
    .content();
```

这样只拿到了：

```text
最终文字
```

但真正的模型响应还包含：

```text
模型名称
Response ID
Prompt Token
Completion Token
Total Token
Finish Reason
```

这些都在：

```text
ChatResponse
↓
ChatResponseMetadata
↓
Usage
```

---

# 二、ChatOptions 是什么

`ChatOptions` 是常用聊天生成参数的统一抽象。

常见：

```text
model
temperature
maxTokens
topP
stopSequences
```

Spring AI 2.0.1 可以：

```java
ChatOptions.Builder<?> options = ChatOptions.builder()
    .temperature(0.7)
    .maxTokens(500);
```

然后只覆盖本次请求：

```java
chatClientBuilder
    .build()
    .prompt()
    .user(message)
    .options(options)
    .call();
```

这就是：

```text
Runtime Options
请求级参数
```

而 `application.yml` 里的参数属于：

```text
Default Options
默认参数
```

---

# 三、注意 Thinking Mode

如果底层模型开启了思考模式，有些模型会忽略：

```text
temperature
```

所以如果看到类似：

```text
Thinking mode does not support temperature
```

不是 Spring AI 崩了，而是：

```text
模型本身在当前模式不使用该参数
```

快速学习阶段知道即可。

---

# 四、怎么拿完整 ChatResponse

不要：

```java
.content()
```

而是：

```java
ChatResponse response = chatClientBuilder
    .build()
    .prompt()
    .user(message)
    .options(options)
    .call()
    .chatResponse();
```

---

# 五、读取 Metadata

```java
ChatResponseMetadata metadata =
    response.getMetadata();
```

常用：

```java
metadata.getId();
metadata.getModel();
metadata.getUsage();
```

---

# 六、读取 Token Usage

```java
Usage usage = metadata.getUsage();
```

然后：

```java
usage.getPromptTokens();
usage.getCompletionTokens();
usage.getTotalTokens();
```

对应：

```text
Prompt Tokens
→ 输入消耗

Completion Tokens
→ 输出消耗

Total Tokens
→ 总消耗
```

这是以后：

```text
成本统计
限额
监控
计费
```

最基础的数据来源之一。

---

# 七、读取 Finish Reason

```java
String finishReason = response
    .getResult()
    .getMetadata()
    .getFinishReason();
```

常见含义大致是：

```text
stop
→ 正常结束

length
→ 达到最大生成长度
```

实际值由具体模型供应商决定。

---

# 八、测试接口

```http
POST http://localhost:8080/lesson23/chat
Content-Type: application/json

{
  "message": "用三句话解释Spring AI",
  "temperature": 0.7,
  "maxTokens": 300
}
```

返回会包含：

```text
content
responseId
model
promptTokens
completionTokens
totalTokens
finishReason
```

---

# 九、Spring AI 基础知识地图

到这里，不要继续往脑子里塞零散 API。

把 Spring AI 压缩成 5 个模块即可。

## 1. 聊天核心

```text
ChatModel
ChatClient
Prompt
PromptTemplate
Structured Output
Streaming
Chat Memory
Advisor
```

解决：

```text
怎么和大模型聊天
怎么组织 Prompt
怎么流式输出
怎么记住上下文
怎么扩展调用链
```

## 2. 工具能力

```text
Tool Calling
MCP Server
MCP Client
```

解决：

```text
AI 怎么调用 Java 方法
AI 怎么连接外部工具
```

## 3. 知识库 / RAG

```text
Embedding
VectorStore
Document
TextSplitter
DocumentReader
RAG
```

解决：

```text
怎么让 AI 使用自己的知识
```

完整主线：

```text
PDF
↓
DocumentReader
↓
Document
↓
TextSplitter
↓
Embedding
↓
VectorStore
↓
Retrieval
↓
DeepSeek
```

## 4. 其他模型能力

```text
Multimodal
ImageModel
Audio
Moderation
```

这些是并列能力。

快速学习阶段：

```text
知道有、知道接口、用到再深入
```

## 5. 框架辅助能力

```text
Observability
Evaluation
Model Options
Metadata
Usage
```

解决：

```text
怎么观察 AI
怎么评价 AI
怎么控制参数
怎么统计 Token
```

---

# 十、现在真正重要的是什么

以后做 Agent，最常组合的是：

```text
LLM
+
Memory
+
Tool
+
MCP
+
RAG
+
Workflow / Agent
```

所以你不需要把 Spring AI 每个 API 背下来。

你现在只需要做到：

```text
看到一个能力
→ 知道它解决什么
→ 知道在哪一层
→ 真正用时能查官方文档
```

---

# 十一、Lesson01～23 最终路线

```text
01 ChatModel
02 ChatClient
03 Prompt
04 PromptTemplate
05 Structured Output
06 Streaming
07 Chat Memory
08 Tool Calling
09 Advisor
10 Embedding
11 VectorStore
12 Document / TextSplitter
13 RAG
14 DocumentReader / PDF
15 MCP Server
16 MCP Client
17 Multimodal
18 ImageModel
19 Audio
20 Moderation
21 Observability
22 Evaluation
23 Model Options / Metadata / Usage
```

到 Lesson23：

# Spring AI 基础阶段结束。

---

# 十二、下一阶段：Spring AI Alibaba

接下来不再继续堆 Spring AI 基础 API。

正式进入：

```text
Spring AI Alibaba
↓
Agent
↓
Agent State / Memory
↓
Tool
↓
MCP
↓
RAG
↓
Graph / Workflow
↓
Multi-Agent
↓
完整 Agent 项目
```

Spring AI 是你的：

```text
模型能力基础
```

Spring AI Alibaba 接下来重点学习：

```text
如何把这些基础能力组织成 Agent 应用
```

---

# 十三、必须会回答

```text
ChatOptions 是什么？
→ 请求级 / 默认聊天参数统一抽象。

ChatResponse 和 content() 区别？
→ content() 只拿文字；ChatResponse 能拿完整结果和 metadata。

Usage 有什么？
→ promptTokens、completionTokens、totalTokens。

Spring AI 基础最重要的三块？
→ 聊天、工具、RAG。
```

---

# 官方参考

- ChatClient: https://docs.spring.io/spring-ai/reference/api/chatclient.html
- AI Metadata: https://docs.spring.io/spring-ai/reference/api/aimetadata.html
- ChatOptions: https://docs.spring.io/spring-ai/docs/current/api/org/springframework/ai/chat/prompt/ChatOptions.html
