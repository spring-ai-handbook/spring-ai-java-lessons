package com.example.aidemo1.lesson08;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Lesson08：订单 Tool。
 *
 * <p>Tool 是 AI 可以选择调用的能力入口，但不替代业务 Service。
 * userId 不由模型生成，而是通过 ToolContext 从 Java 后端可信上下文传入。</p>
 */
@Component
@RequiredArgsConstructor
public class Lesson08OrderTool {

    /** ToolContext 中保存当前登录用户 ID 的 Key。 */
    public static final String USER_ID = "userId";

    /**
     * 真实订单业务服务。
     */
    private final Lesson08OrderService orderService;

    /**
     * 根据订单编号查询当前登录用户的订单详情。
     *
     * @param orderId 模型从用户自然语言中提取的订单编号
     * @param toolContext Java 后端提供的可信 ToolContext
     * @return 订单详情
     */
    @Tool(description = "根据订单编号查询当前登录用户的订单状态、商品和物流信息")
    public Lesson08OrderInfo getOrderStatus(
            @ToolParam(description = "订单编号，例如 10001") Long orderId,
            ToolContext toolContext) {
        Long userId = getCurrentUserId(toolContext);
        return orderService.getOrder(orderId, userId);
    }

    /**
     * 查询当前登录用户的订单列表。
     *
     * @param status 可选订单状态；用户未指定状态时模型可以不传
     * @param toolContext Java 后端提供的可信 ToolContext
     * @return 当前用户订单列表
     */
    @Tool(description = "查询当前登录用户的订单列表，可按订单状态筛选")
    public List<Lesson08OrderInfo> listOrders(
            @ToolParam(
                    description = "订单状态，可选值：PENDING_PAYMENT、PAID、SHIPPED、COMPLETED、CANCELLED",
                    required = false
            ) Lesson08OrderStatus status,
            ToolContext toolContext) {
        Long userId = getCurrentUserId(toolContext);
        return orderService.listOrders(userId, status);
    }

    /**
     * 演示 returnDirect=true。
     *
     * <p>该 Tool 的返回值不会再交给 LLM 进行自然语言整理，
     * 而是直接作为 ChatClient 的最终结果返回。</p>
     *
     * @param orderId 订单编号
     * @param toolContext Java 后端提供的可信 ToolContext
     * @return 原始订单对象
     */
    @Tool(
            description = "直接返回订单原始数据，仅当用户明确要求查看原始订单数据时使用",
            returnDirect = true
    )
    public Lesson08OrderInfo getRawOrder(
            @ToolParam(description = "订单编号") Long orderId,
            ToolContext toolContext) {
        Long userId = getCurrentUserId(toolContext);
        return orderService.getOrder(orderId, userId);
    }

    /**
     * 从 ToolContext 获取当前登录用户 ID。
     *
     * @param toolContext Tool 执行上下文
     * @return 当前登录用户 ID
     */
    private Long getCurrentUserId(ToolContext toolContext) {
        Object userId = toolContext.getContext().get(USER_ID);

        if (userId == null) {
            throw new IllegalStateException("ToolContext 缺少 userId");
        }

        return Long.valueOf(String.valueOf(userId));
    }
}
