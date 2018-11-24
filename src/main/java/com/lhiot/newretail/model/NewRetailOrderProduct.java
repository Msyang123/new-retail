package com.lhiot.newretail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yijun
 **/
@Data
@ApiModel
public class NewRetailOrderProduct {

    private Long id;
    @ApiModelProperty(notes = "商品编码", dataType = "String", required = true)
    private String barcode;
    @ApiModelProperty(notes = "折扣后金额 如果没有请设置与totalPrice一致", dataType = "Integer", required = true)
    private Integer discountPrice;
    @ApiModelProperty(notes = "商品名称", dataType = "String")
    private String productName;
    @ApiModelProperty(notes = "商品数量", dataType = "BigDecimal")
    private BigDecimal productQty;
    @ApiModelProperty(notes = "规格数量", dataType = "BigDecimal")
    private BigDecimal specificationQty;
    @ApiModelProperty(notes = "单个商品总价", dataType = "Integer", required = true)
    private Integer totalPrice;
    @ApiModelProperty(notes = "单个商品总重量 单位（kg）", dataType = "BigDecimal")
    private BigDecimal totalWeight;

    @JsonIgnore
    private String orderCode;

    @JsonIgnore
    private Long specificationId;

    @ApiModelProperty(notes = "孚利购商品模板id 我们推送的时候传递barcode", dataType = "String", required = true)
    private String templateId;
}
