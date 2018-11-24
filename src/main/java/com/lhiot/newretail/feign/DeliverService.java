package com.lhiot.newretail.feign;

import com.leon.microx.web.result.Tips;
import com.lhiot.newretail.feign.model.DeliverOrder;
import com.lhiot.newretail.feign.model.DeliverUpdate;
import com.lhiot.newretail.feign.type.CoordinateSystem;
import com.lhiot.newretail.feign.type.DeliverType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Component
@FeignClient("delivery-service-v1-0")
public interface DeliverService {

    //发送配送单
    @RequestMapping(value = "/{deliverType}/delivery-notes", method = RequestMethod.POST)
    ResponseEntity create(@PathVariable("deliverType") DeliverType type, @RequestParam("coordinate") CoordinateSystem coordinate, @RequestBody DeliverOrder deliverOrder);

    //更新配送单
    @RequestMapping(value = "/delivery-notes/{code}", method = RequestMethod.PUT)
    ResponseEntity<String> update(@PathVariable("code") String code, @RequestBody DeliverUpdate deliverUpdate);

    //配送单详细信息
    @RequestMapping(value = "/{deliverType}/delivery-notes/{code}", method = RequestMethod.GET)
    ResponseEntity<String> detail(@PathVariable("deliverType") DeliverType type, @PathVariable("code") String code);

    //配送单回调签名验证
    @RequestMapping(value = "/{deliverType}/back-signature", method = RequestMethod.POST)
    ResponseEntity<Tips> backSignature(@PathVariable("deliverType") DeliverType type, @RequestBody Map<String,String> params);

}
