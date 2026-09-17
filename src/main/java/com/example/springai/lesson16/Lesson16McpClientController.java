package com.example.springai.lesson16;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lesson16 MCP Client 最小调用示例。
 *
 * <p>本课程演示：</p>
 *
 * <pre>
 * 用户问题
 * ↓
 * ChatClient
 * ↓
 * DeepSeek
 * ↓
 * MCP Client
 * ↓
 * MCP Server
 * ↓
 * MCP Tool
 * </pre>
 *
 * <p>Spring AI 会根据 application.yml
 * 自动连接 MCP Server，并将远程 Tool
 * 包装成 ToolCallbackProvider。</p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson16")
@Profile("mcp-client")
public class Lesson16McpClientController {
    /**
     * Spring AI 自动配置好的 ChatClient Builder。
     *
     * <p>当前项目底层 ChatModel 仍然是 DeepSeek。</p>
     */
    private final ChatClient.Builder chatClientBuilder;
    /**
     * MCP Server 提供的远程工具集合。
     *
     * <p>Spring AI MCP Client 会自动：</p>
     *
     * <pre>
     * 连接 MCP Server
     * ↓
     * 查询 Tool 列表
     * ↓
     * 转成 ToolCallback
     * ↓
     * 包装为 SyncMcpToolCallbackProvider
     * </pre>
     */
    private final SyncMcpToolCallbackProvider mcpTools;

    /**
     * 使用 DeepSeek + MCP Tool 完成聊天。
     *
     * @param message 用户问题
     * @return DeepSeek 最终回答
     */

    @GetMapping("/chat")
    public String chat(
        @RequestParam String message
    ) {
        return chatClientBuilder.build().prompt().user(message)
            .tools(mcpTools).call().content();
    }

}
