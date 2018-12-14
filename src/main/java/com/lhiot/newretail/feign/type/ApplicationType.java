package com.lhiot.newretail.feign.type;

import lombok.Getter;

/**
 * 应用类型
 */
public enum ApplicationType {
    NEW_RETAIL("新零售");
    @Getter
    private String description;

    ApplicationType(String description) {
        this.description = description;
    }
}
