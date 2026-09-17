# Spring AI Alibaba：第00课——版本选择与学习路线

> 日期：2026-09-16  
> 目标：在正式写 ReactAgent 之前，先避免 Spring AI / Spring Boot 版本混用。

## 1. 当前 Spring AI 基础项目

你刚刚完成的基础项目是：

```text
Spring Boot 4.1.1
Spring AI 2.0.1
JDK 21
```

这个项目已经作为 Spring AI 基础课程归档，不再往里面硬塞 Spring AI Alibaba。

## 2. 当前 Spring AI Alibaba 官方版本情况

截至 2026-09-16：

```text
Spring AI Alibaba 最新稳定版：1.1.2.2
```

官方主仓库 1.1.2.2 对齐：

```text
Spring Boot 3.5.8
Spring AI 1.1.2
Spring AI Alibaba 1.1.2.2
```

另外官方存在：

```text
Spring AI Alibaba 2.0.0-M1.1
```

但它仍然是 Pre-release，并且对齐的是：

```text
Spring Boot 4.0.0
Spring AI 2.0.0-M1
```

并不是你当前的：

```text
Spring Boot 4.1.1
Spring AI 2.0.1
```

因此不要直接把稳定版 `spring-ai-alibaba-agent-framework:1.1.2.2` 塞进当前 Spring AI 2.0.1 项目。

## 3. 为什么不混用

官方 Issue 中已经出现过：

```text
Spring AI Alibaba 1.1.2.2
+
Spring AI 2.0.x / Spring Boot 4
```

导致 Jackson 2 / Jackson 3 等依赖冲突的问题。

所以学习阶段最稳妥的办法：

```text
项目A
Spring AI 2.0.1 基础课程
→ 已完成，冻结

项目B
Spring AI Alibaba 1.1.2.2
→ 单独创建
→ 专门学习 Agent
```

## 4. 我们接下来采用的版本

为了快速学习并减少兼容问题，主线采用官方稳定组合：

```text
JDK 21
Spring Boot 3.5.8
Spring AI 1.1.2
Spring AI Alibaba 1.1.2.2
```

JDK 21 可以继续使用，不需要降到 JDK 17。

等 Spring AI Alibaba 2.x 正式稳定并对齐 Spring AI 2.x 后，再单独做升级迁移。

## 5. Spring AI Alibaba 学习重点

Spring AI 基础阶段已经学过：

```text
ChatModel
ChatClient
Memory
Tool Calling
MCP
RAG
```

所以 Alibaba 阶段不再重讲这些基础。

重点转向：

```text
ReactAgent
↓
Agent + Tool
↓
Agent Memory / State
↓
Hooks
↓
Structured Output
↓
Agentic RAG
↓
StateGraph
↓
Workflow
↓
Multi-Agent
↓
Routing / Supervisor / Handoff
↓
完整 Agent 项目
```

## 6. 快速课程规划

```text
Alibaba Lesson01
ReactAgent 最小可运行示例

Alibaba Lesson02
ReactAgent + Tool

Alibaba Lesson03
Agent Memory / threadId / MemorySaver

Alibaba Lesson04
Hooks / ModelCallLimit

Alibaba Lesson05
Structured Output

Alibaba Lesson06
Agentic RAG

Alibaba Lesson07
StateGraph：State / Node / Edge

Alibaba Lesson08
Conditional Edge / Workflow

Alibaba Lesson09
Multi-Agent：Routing / Supervisor

Alibaba Lesson10
完整 Agent 项目
```

仍然采用你的学习方式：

```text
每课新增文件
不覆盖上一课
Java 完整中文注释
@RequiredArgsConstructor 优先
每课直接生成 Markdown 文件
先学核心，再深入项目
```

## 7. 下一课

正式开始：

```text
Spring AI Alibaba Lesson01：ReactAgent 最小入门
```

只解决：

```text
ChatModel
↓
ReactAgent.builder()
↓
agent.call()
↓
AssistantMessage
```

先让第一个 Agent 跑起来，再继续 Tool / Memory / Graph。

## 官方资料

- Spring AI Alibaba Releases: https://github.com/alibaba/spring-ai-alibaba/releases
- Spring AI Alibaba 官方文档: https://java2ai.com/docs/overview/
- ReactAgent 快速开始: https://java2ai.com/docs/quick-start/
- 版本说明: https://java2ai.com/docs/versions/
