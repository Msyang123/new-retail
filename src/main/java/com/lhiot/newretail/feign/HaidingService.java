package com.lhiot.newretail.feign;

import com.lhiot.newretail.feign.model.HdOrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Component
@FeignClient("thirdparty-service-v1-0")
public interface HaidingService {

    /**
     * 门店减库存
     * @param orderInfo
     * @return
     */
    @RequestMapping(value = "/hd/inventory", method = RequestMethod.PUT)
    ResponseEntity<String> reduce(@RequestBody HdOrderInfo orderInfo);

    /**
     * 门店查库存
     * @param storeCode
     * @param skuIds
     * @return
     */
    @RequestMapping(value = "/hd/sku/{storeCode}", method = RequestMethod.POST)
    ResponseEntity<Map<String, Object>> querySku(@PathVariable("storeCode") String storeCode, @RequestBody String[] skuIds);
}
