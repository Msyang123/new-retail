package com.lhiot.newretail.feign.type;

import lombok.Getter;

/**
 * 门店类型枚举
 */
public enum StoreType {
    ORDINARY_STORE("普通门店"),
    FLAGSHIP_STORE ("旗舰店");


    @Getter
    private String  decription;

    StoreType(String decription) {
        this.decription = decription;
    }
}
