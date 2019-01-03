package com.lhiot.newretail.feign.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lhiot.newretail.feign.type.OrderRefundStatus;
import com.lhiot.newretail.feign.type.RefundType;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

/**
 * @author zhangfeng created in 2018/9/25 17:16
 **/
@Data
public class OrderRefund {
    private Long id;
    private Long orderId;
    private String hdOrderCode;
    private Long userId;
    private String orderProductIds;
    private String reason;
    private OrderRefundStatus orderRefundStatus;
    private RefundType refundType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyAt = Date.from(Instant.now());
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date disposeAt;
}
