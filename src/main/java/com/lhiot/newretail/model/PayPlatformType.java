package com.lhiot.newretail.model;

import lombok.Getter;

public enum PayPlatformType {
    wechat("微信支付"),
    alipay("支付宝支付");

    @Getter
    private String decription;

    PayPlatformType(String decription) {
        this.decription = decription;
    }
}