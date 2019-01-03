package com.lhiot.newretail.feign.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lhiot.newretail.feign.type.OrderStatus;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 订单操作流水记录
 */
@EqualsAndHashCode(callSuper = false)
@Data
@ApiModel
public class OrderFlow extends PagerRequestObject {
    private Long id;
    private Long orderId;
    private OrderStatus status;
    private OrderStatus preStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createAt;
}
