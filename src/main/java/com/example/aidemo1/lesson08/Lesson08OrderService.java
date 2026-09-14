package com.example.aidemo1.lesson08;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lesson08：模拟真实订单业务层。
 *
 * <p>当前使用 ConcurrentHashMap 模拟数据库。
 * Tool 只负责作为 AI Adapter，真实查询和业务校验仍然放在 Service 中。</p>
 */
@Service
public class Lesson08OrderService {

    /**
     * 模拟订单数据库。
     */
    private final Map<Long, Lesson08OrderInfo> orderStore = new ConcurrentHashMap<>();

    /**
     * 初始化演示订单数据。
     */
    @PostConstruct
    public void init() {
        orderStore.put(10001L, new Lesson08OrderInfo(
                10001L,
                9001L,
                "机械键盘",
                Lesson08OrderStatus.SHIPPED,
                "顺丰运输中，预计明天送达"
        ));

        orderStore.put(10002L, new Lesson08OrderInfo(
                10002L,
                9001L,
                "27 英寸显示器",
                Lesson08OrderStatus.PAID,
                "商家备货中，暂未生成物流单号"
        ));

        orderStore.put(20001L, new Lesson08OrderInfo(
                20001L,
                9002L,
                "无线鼠标",
                Lesson08OrderStatus.COMPLETED,
                "已签收"
        ));
    }

    /**
     * 按订单编号查询订单，并校验订单归属。
     *
     * @param orderId 订单编号
     * @param currentUserId 当前登录用户 ID
     * @return 当前用户拥有的订单
     */
    public Lesson08OrderInfo getOrder(Long orderId, Long currentUserId) {
        Lesson08OrderInfo order = orderStore.get(orderId);

        if (order == null) {
            throw new IllegalArgumentException("订单不存在：" + orderId);
        }

        if (!order.userId().equals(currentUserId)) {
            throw new IllegalArgumentException("当前用户无权访问该订单");
        }

        return order;
    }

    /**
     * 查询当前用户指定状态的订单。
     *
     * @param currentUserId 当前登录用户 ID
     * @param status 订单状态；为空时查询该用户全部订单
     * @return 匹配订单列表
     */
    public List<Lesson08OrderInfo> listOrders(
            Long currentUserId,
            Lesson08OrderStatus status) {
        return orderStore.values()
                .stream()
                .filter(order -> order.userId().equals(currentUserId))
                .filter(order -> status == null || order.status() == status)
                .toList();
    }
}
