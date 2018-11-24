package com.lhiot.newretail.feign.model;

import com.lhiot.newretail.feign.type.RefundStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangfeng created in 2018/9/19 9:20
 **/
@Data
@ApiModel
public class OrderProduct {

    private Long id;
    private Long orderId;
    @ApiModelProperty(notes = "商品架Id", dataType = "Long")
    private Long shelfId;
    @ApiModelProperty(notes = "商品规格Id", dataType = "Long")
    private Long specificationId;
    @ApiModelProperty(notes = "规格Id对应的条码", dataType = "String")
    private String barcode;
    @ApiModelProperty(notes = "单个商品总价值", dataType = "Integer")
    private Integer totalPrice;
    @ApiModelProperty(notes = "购买份数", dataType = "Integer")
    private Integer productQty;
    @ApiModelProperty(notes = "规格数量", dataType = "BigDecimal")
    private BigDecimal shelfQty;
    @ApiModelProperty(notes = "除去优惠金额后单个商品总价", dataType = "Integer")
    private Integer discountPrice;
    @ApiModelProperty(notes = "商品名称", dataType = "String")
    private String productName;
    @ApiModelProperty(notes = "商品主图", dataType = "String")
    private String image;
    @ApiModelProperty(notes = "单个商品总重量", dataType = "BigDecimal")
    private BigDecimal totalWeight;
    @ApiModelProperty(notes = "是否退货", dataType = "RefundStatus")
    private RefundStatus refundStatus;
}
