package com.lhiot.newretail.feign.model;

import com.lhiot.newretail.feign.type.DeliverStatus;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Leon (234239150@qq.com) created in 11:38 18.11.12
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class DeliverUpdate {
    private String orderId;
    private DeliverStatus deliverStatus;
    private String carrierDriverName;
    private String carrierDriverPhone;
    private String cancelReason;
}
