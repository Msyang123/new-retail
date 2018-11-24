package com.lhiot.newretail.feign.model;

import com.lhiot.newretail.feign.type.AvailableStatus;
import com.lhiot.newretail.feign.type.InventorySpecification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangfeng create in 9:11 2018/11/9
 */
@Data
@ApiModel
public class ProductSpecification {
    @ApiModelProperty(notes = "主键Id", dataType = "Long")
    private Long id;
    @ApiModelProperty(notes = "商品ID", dataType = "Long")
    private Long productId;
    @ApiModelProperty(notes = "商品条码", dataType = "String")
    private String barcode;
    @ApiModelProperty(notes = "打包单位", dataType = "String")
    private String packagingUnit;
    @ApiModelProperty(notes = "单份规格商品的重量", dataType = "BigDecimal")
    private BigDecimal weight;
    @ApiModelProperty(notes = "海鼎规格数量", dataType = "BigDecimal")
    private BigDecimal specificationQty;
    @ApiModelProperty(notes = "安全库存", dataType = "Integer")
    private Integer limitInventory;
    @ApiModelProperty(notes = "是否为库存规格：YES-是，NO-否", dataType = "InventorySpecification")
    private InventorySpecification inventorySpecification;
    @ApiModelProperty(notes = "是否可用：YES-可用，NO-不可用", dataType = "AvailableStatus")
    private AvailableStatus availableStatus;
    @ApiModelProperty(notes = "创建时间", dataType = "Date", readOnly = true)
    private Date createAt;
}
