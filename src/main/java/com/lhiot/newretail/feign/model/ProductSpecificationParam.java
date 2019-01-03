package com.lhiot.newretail.feign.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lhiot.newretail.feign.type.AvailableStatus;
import com.lhiot.newretail.feign.type.InventorySpecification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xiaojian  created in  2018/11/19 8:40
 */
@ApiModel
@Data
public class ProductSpecificationParam {
    @ApiModelProperty(notes = "商品ID", dataType = "Long")
    private Long productId;
    @ApiModelProperty(notes = "商品条码", dataType = "String")
    private String barCodes;
    @ApiModelProperty(notes = "打包单位", dataType = "String")
    private String packagingUnit;
    @ApiModelProperty(notes = "是否为库存规格：YES-是，NO-否", dataType = "InventorySpecification")
    private InventorySpecification inventorySpecification;
    @ApiModelProperty(notes = "是否可用：YES-可用，NO-不可用", dataType = "AvailableStatus")
    private AvailableStatus availableStatus;
    @ApiModelProperty(notes = "起始创建时间", dataType = "Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginCreateAt;
    @ApiModelProperty(notes = "截止创建时间", dataType = "Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endCreateAt;
    @ApiModelProperty(notes = "查询条数", dataType = "Integer")
    private Integer rows;
    @ApiModelProperty(notes = "当前页", dataType = "Integer")
    private Integer pages;

    @ApiModelProperty(hidden = true)
    private Integer startRow;
}
