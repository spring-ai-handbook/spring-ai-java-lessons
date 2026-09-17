package com.example.springai.lesson15;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Lesson15 MCP 工具示例。
 *
 * <p>这个类演示如何通过 Spring AI 2.0.1 的 MCP 注解，
 * 把普通 Java 方法暴露给 MCP Client。</p>
 */
@Component
@Profile("mcp-server")
public class Lesson15McpTools {
    /**
     * 查询城市天气。
     *
     * <p>这里只返回固定模拟数据，
     * 目的是快速理解 MCP Tool。</p>
     *
     * @param city 城市名称
     * @return 模拟天气
     */
    @McpTool(
        name = "getWeather",
        description = "查询指定城市的天气"
    )
    public String getWeather(
        @McpToolParam(
            description = "城市名称",
            required = true
        ) String city
    ) {
        return city + "今天晴,温度28C";
    }


    /**
     * 查询模拟订单状态。
     *
     * @param orderNo 订单号
     * @return 模拟订单状态
     */
    @McpTool(
        name = "getOrder",
        description = "根据订单号查询订单状态"
    )
    public String getOrder(
        @McpToolParam(
            description = "订单号",
            required = true
        )
        String orderNo) {

        return "订单 " + orderNo + " 当前状态：已发货";
    }
}
