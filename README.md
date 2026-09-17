# Spring AI Java Lessons

> Spring AI Handbook 的 Java / Spring AI 2.0.1 基础课程代码仓库。

本仓库采用“课程累积式”组织方式：每学习一个新知识点，就新增独立 `lessonXX` 包；前面课程代码保留，不覆盖、不删除。当前基础主线已经整理到 **Lesson23**，完成后即可进入 Spring AI Alibaba。

## 技术栈

- JDK 21
- Spring Boot 4.1.1
- Spring AI 2.0.1
- Maven
- DeepSeek ChatModel
- OpenAI-compatible EmbeddingModel
- WebFlux / SSE
- MCP
- Spring Boot Actuator
- Lombok

## 完整课程目录

| Lesson | 主题 | 核心内容 |
| --- | --- | --- |
| 01 | ChatModel | 模型统一抽象与基础调用 |
| 02 | ChatClient | Fluent API |
| 03 | Prompt | System / User Prompt |
| 04 | PromptTemplate | Prompt 模板变量 |
| 05 | Structured Output | DTO / List 结构化输出 |
| 06 | Streaming | Flux / SSE |
| 07 | Chat Memory | 多轮会话 |
| 08 | Tool Calling | `@Tool` |
| 09 | Advisor | Advisor 调用链 |
| 10 | Embedding | 文本转向量 |
| 11 | VectorStore | 向量存储与相似度查询 |
| 12 | Document / TextSplitter | Document 与文本切片 |
| 13 | RAG | Retrieval + Augmentation + Generation |
| 14 | DocumentReader / PDF | PDF 知识导入 |
| 15 | MCP Server | `@McpTool` |
| 16 | MCP Client | 远程 MCP Tool 调用 |
| 17 | Multimodal | 文本 + 媒体输入概念 |
| 18 | ImageModel | 图片生成模型接口 |
| 19 | Audio | TTS / STT |
| 20 | Moderation | 内容审核 |
| 21 | Observability | Metrics / Token / 调用耗时 |
| 22 | Evaluation | RelevancyEvaluator |
| 23 | Model Options / Metadata / Usage | 请求参数、Token、Response Metadata、基础收尾 |

## 最终知识地图

```text
聊天核心
ChatModel / ChatClient / Prompt / Memory / Advisor

工具能力
Tool Calling / MCP

知识库
Embedding / VectorStore / Document / TextSplitter / DocumentReader / RAG

扩展模型
Multimodal / Image / Audio / Moderation

辅助能力
Observability / Evaluation / Options / Metadata / Usage
```

## 运行环境

至少配置：

```powershell
$env:DEEPSEEK_API_KEY="你的 DeepSeek API Key"
$env:OPENAI_EMBEDDING_API_KEY="你的 Embedding API Key"
```

如果你使用其他 OpenAI-compatible Embedding 服务，可以继续覆盖：

```text
OPENAI_EMBEDDING_BASE_URL
OPENAI_EMBEDDING_MODEL
OPENAI_EMBEDDING_DIMENSIONS
```

默认启动：

```bash
mvn spring-boot:run
```

默认地址：

```text
http://localhost:8080
```

## MCP 两进程测试

Server：

```text
Profile: mcp-server
Port: 8081
```

Client：

```text
Profile: mcp-client
Port: 8080
```

IDEA 的 Active profiles 分别填写：

```text
mcp-server
```

和：

```text
mcp-client
```

## 可选模型能力

为了让基础项目在没有额外 OpenAI Key 时也能正常启动，默认关闭：

```yaml
spring:
  ai:
    model:
      image: none
      moderation: none
      audio:
        speech: none
        transcription: none
```

Lesson18、19、20 使用 `@ConditionalOnBean`，因此这些模型没有开启时不会影响主项目启动。真正测试对应课程时再临时开启即可。

## Lesson21 Actuator

项目已加入 Actuator。调用一次：

```text
GET /lesson21/chat
```

然后可以查看：

```text
GET /actuator/metrics
```

## 教学文档

项目 `docs/` 中已经包含最后四课：

```text
Lesson20_Moderation内容审核快速入门.md
Lesson21_Observability快速入门.md
Lesson22_Evaluation快速入门.md
Lesson23_ModelOptions_Metadata_Usage_SpringAI基础收尾.md
```

其中 Lesson23 已经把 Spring AI 基础知识地图完整收束。

## 安全说明

项目中不再保留硬编码 API Key。所有模型密钥统一从环境变量读取。

## 下一阶段

完成 Lesson23 后，不再继续堆 Spring AI 基础 API，正式进入：

```text
Spring AI Alibaba
↓
Agent
↓
Tool / MCP / RAG
↓
Graph / Workflow
↓
Multi-Agent
↓
完整 Agent 项目
```
