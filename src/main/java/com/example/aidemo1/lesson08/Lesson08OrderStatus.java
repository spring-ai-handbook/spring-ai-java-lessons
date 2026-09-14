package com.example.aidemo1.lesson08;

/**
 * Lesson08：订单状态枚举。
 *
 * <p>有限业务值优先使用 enum，可以让 Tool JSON Schema 更明确，
 * 减少模型自由生成不存在状态值的概率。</p>
 */
public enum Lesson08OrderStatus {

    /** 待支付。 */
    PENDING_PAYMENT,

    /** 已支付，等待发货。 */
    PAID,

    /** 已发货。 */
    SHIPPED,

    /** 已完成。 */
    COMPLETED,

    /** 已取消。 */
    CANCELLED
}
