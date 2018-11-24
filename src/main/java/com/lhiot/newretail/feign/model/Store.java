package com.lhiot.newretail.feign.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lhiot.newretail.feign.type.ApplicationType;
import com.lhiot.newretail.feign.type.StoreStatus;
import com.lhiot.newretail.feign.type.StoreType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:门店实体类
 */
@Data
@ApiModel
public class Store {

    @ApiModelProperty(notes = "门店id", dataType = "Long")
    private Long id;

    @ApiModelProperty(notes = "门店编码", dataType = "String")
    private String code;

    @ApiModelProperty(notes = "门店名称", dataType = "String")
    private String name;

    @ApiModelProperty(notes = "门店地址", dataType = "String")
    private String address;

    @ApiModelProperty(notes = "联系方式", dataType = "String")
    private String phone;

    @ApiModelProperty(notes = "门店图片", dataType = "String")
    private String image;

    @ApiModelProperty(notes = "所属区域", dataType = "String")
    private String area;

    @ApiModelProperty(notes = "门店状态 ENABLED(\"营业\"),DISABLED(\"未营业\");", dataType = "StoreStatus")
    private StoreStatus status;

    @ApiModelProperty(notes = "旗舰店ID", dataType = "Long")
    private Long flagShip;

    /**
     * 门店类型：00-普通门店  01-旗舰店
     */
    @ApiModelProperty(notes = "门店类型：ORDINARY_STORE(\"普通门店\"),FLAGSHIP_STORE (\"旗舰店\");", dataType = "StoreType")
    private StoreType storeType;

    /**
     * 门店视频
     */
    @ApiModelProperty(notes = "门店视频", dataType = "String")
    private String videoUrl;

    /**
     * 直播开始时间
     */
    @ApiModelProperty(notes = "直播开始时间", dataType = "String")
    private String beginAt;

    /**
     * 直播结束时间
     */
    @ApiModelProperty(notes = "直播结束时间", dataType = "String")
    private String endAt;

    /**
     * 录播地址
     */
    @ApiModelProperty(notes = "录播地址", dataType = "String")
    private String tapeUrl;

    @ApiModelProperty(notes = "纬度", dataType = "BigDecimal")
    private BigDecimal latitude;


    @ApiModelProperty(notes = "经度", dataType = "BigDecimal")
    private BigDecimal longitude;

    @ApiModelProperty(notes = "启用该门店的应用数组")
    private ApplicationType[] applicationTypes;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Double distance;
}
