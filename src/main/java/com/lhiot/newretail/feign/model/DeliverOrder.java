package com.lhiot.newretail.feign.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leon.microx.util.Jackson;
import com.lhiot.newretail.feign.type.ApplicationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author Leon (234239150@qq.com) created in 8:53 18.11.11
 */


@Data
@ApiModel
@ToString
public class DeliverOrder {

    @ApiModelProperty(value = "id", dataType = "Long")
    private Long id;

    @ApiModelProperty(value = "订单id", dataType = "Long")
    private Long orderId;

    @ApiModelProperty(value = "订单编码", dataType = "String")
    private String orderCode;

    @ApiModelProperty(value = "应用类型:APP(视食),WECHAT_MALL(微商城),S_MALL(小程序),F_MALL(鲜果师)", dataType = "ApplicationType")
    private ApplicationType applyType;

    @ApiModelProperty(value = "门店编码", dataType = "String")
    private String storeCode;
    @ApiModelProperty(value = "门店名称", dataType = "String")
    private String storeName;

    @ApiModelProperty(value = "创建时间", dataType = "Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createAt;
    @ApiModelProperty(value = "海鼎订单编码", dataType = "String")
    private String hdOrderCode;
    @ApiModelProperty(value = "业务回调地址", dataType = "String")
    private String backUrl;

    @ApiModelProperty(value = "订单用户", dataType = "Long")
    private Long userId;
    @ApiModelProperty(value = "订单总价", dataType = "Integer")
    private Integer totalAmount;
    @ApiModelProperty(value = "实收用户配送费", dataType = "Integer")
    private Integer deliveryFee;
    @ApiModelProperty(value = "优惠金额", dataType = "Integer")
    private Integer couponAmount;
    @ApiModelProperty(value = "应付金额", dataType = "Integer")
    private Integer amountPayable;
    @ApiModelProperty(value = "收货人", dataType = "String")
    private String receiveUser;
    @ApiModelProperty(value = "联系方式", dataType = "String")
    private String contactPhone;
    @ApiModelProperty(value = "收货地址", dataType = "String")
    private String address;
    @ApiModelProperty(value = "备注", dataType = "String")
    private String remark;
    @ApiModelProperty(value = "配送时间段", dataType = "DeliverTime")
    private DeliverTime deliverTime;
    @ApiModelProperty(value = "经度", dataType = "Double")
    private Double lng;
    @ApiModelProperty(value = "纬度", dataType = "Double")
    private Double lat;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "配送时间", dataType = "Date")
    private Date deliveryTime;

    @ApiModelProperty(value = "配送订单商品", dataType = "List")
    private List<DeliverProduct> deliverOrderProductList;

    private String getDeliverTimeString() {
        return Jackson.json(this.deliverTime);
    }

    private void setDeliverTimeString(String deliverTimeString) {
        this.deliverTime = Jackson.object(deliverTimeString, DeliverTime.class);
    }
}
