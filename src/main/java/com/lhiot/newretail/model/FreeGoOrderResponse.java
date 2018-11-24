package com.lhiot.newretail.model;


import lombok.Data;

/**
 * 推送新零售门店订单返回结果
 */
@Data
public class FreeGoOrderResponse {

    private String status;

    private String msg;

    private String rst;
}
