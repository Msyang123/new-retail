package com.lhiot.newretail.model;

import lombok.Data;

import java.util.List;

@Data
public class FreeGoOrderRequest {

    private String orderCode;//订单号
    private String shopId;//店铺ID
    private String shopName;//店铺名
    private String orderType;//订单类型	（1001自提 1002 配送）
    private String recrivedName;//收货人姓名
    private String recrivedPhone;//收货人电话
    private String recrivedAddress;//收货人地址如订单为配送必填
    private String remark;//用户备注
    private Integer totalPrice;//总订单金额（必填）单位（分）

    private List<FreeGoOrder> orders;
}
