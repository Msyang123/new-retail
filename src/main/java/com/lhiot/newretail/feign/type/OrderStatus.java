package com.lhiot.newretail.feign.type;

import lombok.Getter;

/**
 * 订单状态
 */
public enum OrderStatus {
    WAIT_PAYMENT("待支付"),
    WAIT_SEND_OUT("待发货"),
    SEND_OUT("已发货"),
    DISPATCHING("配送中"),
    RECEIVED("已收货"),
    RETURNING("退货中"),
    RETURN_FAILURE("退款失败"),
    ALREADY_RETURN("退货完成"),
    FAILURE("已失效"),
    FINISHED("完成");

    @Getter
    private String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
