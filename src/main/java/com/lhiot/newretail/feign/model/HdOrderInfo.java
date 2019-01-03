package com.lhiot.newretail.feign.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lhiot.newretail.feign.type.ApplicationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author Leon (234239150@qq.com) created in 10:11 18.9.27
 */
@Data
@ApiModel
public class HdOrderInfo {
    private Long id;
    private String code;
    private Long userId;
    private ApplicationType applyType;
    private Long storeId;
    private String storeCode;
    private String storeName;
    private String receivingWay;
    private Integer totalAmount;
    private Integer amountPayable;
    private Integer deliveryAmount;
    private Integer couponAmount;
    private String hdStatus;
    private String orderStatus;
    @ApiModelProperty(notes = "收货地址：门店自提订单填写门店地址", dataType = "String")
    private String address;

    @ApiModelProperty(notes = "收货人", dataType = "String")
    private String receiveUser;
    @ApiModelProperty(notes = "收货人联系方式", dataType = "String")
    private String contactPhone;
    private String remark;
    @ApiModelProperty(notes = "提货截止时间", dataType = "String")
    private Timestamp deliveryEndTime;

    private String returnReason;
    private String hdOrderCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payAt;

    @ApiModelProperty(notes = "收货地址：门店自提订单填写门店地址", dataType = "OrderProduct")
    private List<HdOrderProduct> orderProducts;
}
