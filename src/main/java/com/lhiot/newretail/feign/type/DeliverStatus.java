package com.lhiot.newretail.feign.type;

import lombok.Getter;

public enum DeliverStatus {

    CREATE("创建"),

    UNRECEIVE("未接单"),

    WAIT_GET("待取货"),

    DELIVERING("配送中"),

    DONE("配送完成"),

    FAILURE("配送失败");

    @Getter
    private String description;

    DeliverStatus(String description) {
        this.description = description;
    }
}