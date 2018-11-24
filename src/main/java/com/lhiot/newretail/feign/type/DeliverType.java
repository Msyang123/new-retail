package com.lhiot.newretail.feign.type;


import lombok.Getter;

public enum DeliverType {

    FENGNIAO("FENGNIAO", "蜂鸟配送"),

    DADA("DADA", "达达配送"),

    MEITUAN("MEITUAN", "美团配送"),

    @Deprecated
    OWN("OWN", "自己配送");

    @Getter
    private String type;

    @Getter
    private String description;

    DeliverType(String type, String description) {
        this.type = type;
        this.description = description;
    }
}
