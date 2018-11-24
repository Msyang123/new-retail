package com.lhiot.newretail.feign.type;

import lombok.Getter;

/**
 * 坐标系
 *
 * @author Leon (234239150@qq.com) created in 9:25 18.9.26
 */
public enum CoordinateSystem {

    TENCENT(1, false, "腾讯坐标系"),
    BAIDU(2, true, "百度坐标系"),
    AMAP(3, false, "高德坐标系");

    @Getter
    private int positionSource;

    @Getter
    private boolean needConvert;

    @Getter
    private String description;

    CoordinateSystem(int positionSource, boolean needConvert, String description) {
        this.positionSource = positionSource;
        this.needConvert = needConvert;
        this.description = description;
    }
}
