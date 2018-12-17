package com.lhiot.newretail.feign;


import com.leon.microx.web.result.Pages;
import com.lhiot.newretail.feign.model.ProductSpecification;
import com.lhiot.newretail.feign.model.ProductSpecificationParam;
import com.lhiot.newretail.feign.model.Store;
import com.lhiot.newretail.feign.type.ApplicationType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient("basic-data-service-v1-0")
public interface BaseDataService {

    //查询基础服务商品规格
    @RequestMapping(value = "/product-specifications/pages", method = RequestMethod.POST)
    ResponseEntity<Pages<ProductSpecification>> search(@RequestBody ProductSpecificationParam param);

    @RequestMapping(value = "/stores/code/{code}",method = RequestMethod.GET)
    ResponseEntity<Store> findStoreByCode(@PathVariable("code") String code,@RequestParam("applicationType") ApplicationType applicationType);

}
