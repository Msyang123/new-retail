package com.lhiot.newretail.model;

import lombok.Data;

@Data
public class FreeGoOrder {

    private String barcode;//商品条码
    private Integer num;//商品数量
    private Integer price;//单个商品总价
}
