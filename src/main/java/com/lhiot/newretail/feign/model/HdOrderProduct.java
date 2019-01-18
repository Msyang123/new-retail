package com.lhiot.newretail.feign.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Leon (234239150@qq.com) created in 10:11 18.9.27
 */
@Data
@ApiModel
public class HdOrderProduct {
    private Long id;
    private Long orderId;
    private Long specificationId;
    private String barcode;
    private String productName;
    @ApiModelProperty(notes = "单个商品去除折扣后的总价",dataType = "Integer")
    private Integer discountPrice;
    @ApiModelProperty(notes = "单个商品总价",dataType = "Integer")
    private Integer totalPrice;
    @ApiModelProperty(notes = "购买份数",dataType = "Integer")
    private Integer productQty;

    private BigDecimal specificationQty;//基础的规格传1
    private BigDecimal shelfQty;//基础的规格传1 第三方发送海鼎实际传递此值

    private String refundStatus;
    private Integer standardPrice;
}
