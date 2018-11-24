package com.lhiot.newretail.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class NewRetailOrderStore {

    @ApiModelProperty(notes = "操作人", dataType = "String")
    private String operationUser;
    @ApiModelProperty(notes = "门店编码", dataType = "String", required = true)
    private String storeCode;
    @ApiModelProperty(notes = "门店名称", dataType = "String")
    private String storeName;
}
