# Spring AI Java Lessons

> Spring AI Handbook 的 Java / Spring AI 基础课程代码仓库。

本仓库采用“课程累积式”组织方式：每学习一个新知识点，就新增独立的 `lessonXX` 包；前面课程代码保留，不覆盖、不删除。它非常适合配合视频逐课学习，也方便学习者随时回看每个 API 在哪一课首次出现。

## 技术栈

- JDK 21
- Spring Boot 4.1.1
- Spring AI 2.0.1
- Maven
- DeepSeek
- Project Reactor / SSE
- Lombok

## 当前课程

| Lesson | 主题 | 核心内容 |
| --- | --- | --- |
| 01 | ChatModel | 模型统一抽象与基础调用 |
| 02 | ChatClient | Fluent API |
| 03 | Prompt | System / User Prompt |
| 04 | PromptTemplate | 模板变量与参数化 Prompt |
| 05 | Structured Output | DTO / List 结构化输出 |
| 06 | Streaming | Flux / SSE 流式输出 |
| 07 | Chat Memory | 多轮会话与 conversationId |
| 08 | Tool Calling | @Tool / ToolContext / Service 分层 |
| 09 | Advisor | Call / Stream / BaseAdvisor 与调用链 |

后续课程会继续增加 Embedding、VectorStore、Document/TextSplitter、RAG、MCP、Agent 等内容。

## 项目结构

```text
src/main/java/com/example/aidemo1/
├── lesson01/
├── lesson02/
├── lesson03/
├── lesson04/
├── lesson05/
├── lesson06/
├── lesson07/
├── lesson08/
└── lesson09/
```

## 运行环境

请先配置 DeepSeek API Key：

### Windows PowerShell

```powershell
$env:DEEPSEEK_API_KEY="你的 DeepSeek API Key"
```

### macOS / Linux

```bash
export DEEPSEEK_API_KEY="你的 DeepSeek API Key"
```

启动：

```bash
mvn spring-boot:run
```

默认地址：`http://localhost:8080`。

## 代码规范

- Java 类、公开方法、关键字段使用中文 Javadoc。
- 依赖注入优先使用 Lombok `@RequiredArgsConstructor`。
- 业务代码依赖 Spring AI 抽象，例如 `ChatModel`、`EmbeddingModel`，避免直接耦合具体供应商实现。
- 每一课新增自己的 Controller / Service / Config / DTO，不覆盖前面课程。
- 演示中的 `userId` 请求参数只用于教学；真实项目应从 JWT / Spring Security 等可信后端上下文获取。

## 相关仓库

- `spring-ai-java-customer-service`：把基础课程能力串成一个完整智能客服实战项目。
- `spring-ai-handbook-docs`：课程笔记、项目实战文档和故障排查站点。

## 安全说明

不要向 GitHub 提交 API Key、Token、密码或其他真实凭据。本项目的模型密钥统一通过环境变量注入。
