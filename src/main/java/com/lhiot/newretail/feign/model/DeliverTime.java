package com.lhiot.newretail.feign.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel
@ToString
public class DeliverTime {

    @ApiModelProperty(notes = "配送动作", dataType = "String", required = true, example = "立即配送")
    private String display;

    @ApiModelProperty(notes = "开始时间", dataType = "Date", required = true, example = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(notes = "结束时间", dataType = "Date", required = true, example = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    public static DeliverTime of(String display, Date start, Date end){
        DeliverTime deliverTime = new DeliverTime();
        deliverTime.display = display;
        deliverTime.startTime = start;
        deliverTime.endTime = end;
        return deliverTime;
    }
}
