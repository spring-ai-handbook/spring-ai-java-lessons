package com.example.aidemo1.lesson08;

/**
 * Lesson08：订单信息 DTO。
 *
 * @param orderId 订单编号
 * @param userId 订单所属用户 ID
 * @param productName 商品名称
 * @param status 当前订单状态
 * @param logistics 物流信息
 */
public record Lesson08OrderInfo(
        Long orderId,
        Long userId,
        String productName,
        Lesson08OrderStatus status,
        String logistics
) {
}
