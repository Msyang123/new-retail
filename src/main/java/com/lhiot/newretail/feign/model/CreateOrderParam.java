package com.lhiot.newretail.feign.model;

import com.lhiot.newretail.feign.type.AllowRefund;
import com.lhiot.newretail.feign.type.ApplicationType;
import com.lhiot.newretail.model.ReceivingWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class CreateOrderParam {

    private Long userId;
    private ApplicationType applicationType;
    private String orderType;
    private ReceivingWay receivingWay;
    private Integer couponAmount = 0;
    private Integer totalAmount;
    @ApiModelProperty(notes = "配送费", dataType = "Integer")
    private Integer deliveryAmount = 0;
    private Integer amountPayable = 0;
    @ApiModelProperty(notes = "收货地址：门店自提订单填写收货地址", dataType = "String")
    private String address;
    @ApiModelProperty(notes = "收货人", dataType = "String")
    private String receiveUser;
    @ApiModelProperty(notes = "收货人昵称", dataType = "String")
    private String nickname;
    @ApiModelProperty(notes = "收货人联系方式", dataType = "String")
    private String contactPhone;
    private String remark;
    @ApiModelProperty(notes = "提货截止时间", dataType = "String")
    private Date deliveryEndAt;
    @ApiModelProperty(notes = "配送时间 json格式如 {\"display\":\"立即配送\",\"startTime\":\"2018-08-15 11:30:00\",\"endTime\":\"2018-08-15 12:30:00\"}", dataType = "String")
    private String deliveryAt;
    @ApiModelProperty(notes = "是否允许退款YES是NO否", dataType = "AllowRefund")
    private AllowRefund allowRefund;
    @ApiModelProperty(notes = "商品列表", dataType = "OrderProduct")
    private List<OrderProduct> orderProducts;
    @ApiModelProperty(notes = "门店信息", dataType = "OrderStoreParam")
    private OrderStore orderStore;
}
