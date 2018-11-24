package com.lhiot.newretail.model;

import lombok.Getter;

public enum ReceivingWay {
    TO_THE_STORE("门店自提"),
    TO_THE_HOME("送货上门");

    @Getter
    private String description;

    ReceivingWay(String description) {
        this.description = description;
    }
}
