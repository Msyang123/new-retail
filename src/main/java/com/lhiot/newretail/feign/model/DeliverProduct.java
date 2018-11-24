package com.lhiot.newretail.feign.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 商品Model
 * @author Leon (234239150@qq.com) created in 8:59 18.11.11
 */
@Data
@ApiModel
@ToString
public class DeliverProduct {

    @ApiModelProperty(value = "配送订单商品id", dataType = "Long")
    private Long id;

    @ApiModelProperty(value = "配送订单id", dataType = "Long")
    private Long deliverBaseOrderId;

    @ApiModelProperty(value = "商品条码", dataType = "String")
    private String barcode;

    @ApiModelProperty(value = "购买价格", dataType = "Integer")
    private Integer price;

    @ApiModelProperty(value = "规格价格", dataType = "Integer")
    private Integer standardPrice;

    @ApiModelProperty(value = "购买份数", dataType = "Integer")
    private Integer productQty;

    @ApiModelProperty(value = "商品数量或者重量(规格表对应的数量或者重量)", dataType = "Double")
    private Double standardQty;

    @ApiModelProperty(value = "商品基础重量)", dataType = "Double")
    private Double baseWeight;

    @ApiModelProperty(value = "除去优惠金额后单个商品的价格", dataType = "Integer")
    private Integer discountPrice;

    @ApiModelProperty(value = "商品名称", dataType = "String")
    private String productName;

    @ApiModelProperty(value = "商品主图", dataType = "String")
    private String image;

    @ApiModelProperty(value = "商品小图标", dataType = "String")
    private String smallImage;

    @ApiModelProperty(value = "大图标", dataType = "String")
    private String largeImage;
}
