package com.lhiot.newretail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yijun
 **/
@Data
@ApiModel
public class NewRetailOrder{


    @JsonIgnore
    private String orderCode;
    @ApiModelProperty(notes = "收货地址", dataType = "String")
    private String address;
    @ApiModelProperty(notes = "应付金额", dataType = "Integer", required = true)
    private Integer amountPayable;
    @ApiModelProperty(notes = "联系电话", dataType = "String")
    private String contactPhone;
    @ApiModelProperty(notes = "优惠金额", dataType = "Integer")
    private Integer couponAmount = 0;
    @ApiModelProperty(notes = "配送费", dataType = "Integer")
    private Integer deliveryAmount = 0;
    @ApiModelProperty(notes = "配送时间段", dataType = "String")
    private String deliverTime;
    @ApiModelProperty(notes = "用户昵称", dataType = "String")
    private String nickname;
    @ApiModelProperty(notes = "收货人姓名", dataType = "String")
    private String receiveUser;
    @ApiModelProperty(notes = "收货方式 TO_THE_STORE门店自提", dataType = "ReceivingWay", required = true)
    private ReceivingWay receivingWay;
    @ApiModelProperty(notes = "用户备注", dataType = "String")
    private String remark;
    @ApiModelProperty(notes = "总订单金额", dataType = "Integer", required = true)
    private Integer totalAmount;
    @ApiModelProperty(notes = "支付类型 alipay-支付宝 wechat-微信", dataType = "PayPlatformType", required = true)
    private PayPlatformType payType;
    @ApiModelProperty(notes = "合作伙伴订单编码", dataType = "String")
    private String partnerCode;


    @ApiModelProperty(notes = "商品列表", dataType = "OrderProduct", required = true)
    private List<NewRetailOrderProduct> orderProducts;

    @ApiModelProperty(notes = "门店信息", dataType = "OrderStore", required = true)
    private NewRetailOrderStore orderStore;

}
