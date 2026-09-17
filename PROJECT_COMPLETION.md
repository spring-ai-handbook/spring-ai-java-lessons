# Spring AI 基础项目整理完成说明

本次按“快速熟悉 Spring AI 全知识点，然后进入 Spring AI Alibaba”的目标完成整理。

## 已完成

- 恢复项目中缺失的 `lesson12` Document / TextSplitter 代码。
- 保留 Lesson01～Lesson20 的累积式课程结构。
- 新增 Lesson21：Observability。
- 新增 Lesson22：Evaluation / RelevancyEvaluator。
- 新增 Lesson23：Model Options / Response Metadata / Usage。
- Lesson23 中完成 Spring AI 基础知识地图收束。
- `pom.xml` 增加 Spring Boot Actuator，用于 Lesson21。
- `application.yml` 增加 Actuator metrics 暴露配置。
- Image / Audio / Moderation 默认保持关闭，避免可选模型 Bean 缺失导致主项目无法启动。
- Lesson18 / Lesson19 / Lesson20 使用 `@ConditionalOnBean`，只有对应模型真正开启时才加载 Controller。
- 移除 `application.yml` 中硬编码的 API Key，改为环境变量。
- 更新 README 到 Lesson23。
- 新增 Lesson20～Lesson23 Markdown 教学文档。

## Spring AI 基础结束点

```text
Lesson23
↓
Spring AI 基础结束
↓
Spring AI Alibaba
↓
Agent / Graph / Workflow / Multi-Agent
```

下一阶段不再继续堆 Spring AI 零散 API，而是正式学习如何把 LLM、Memory、Tool、MCP、RAG 组织成 Agent 应用。
