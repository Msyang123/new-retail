package com.lhiot.newretail.feign;

import com.lhiot.newretail.feign.model.CreateOrderParam;
import com.lhiot.newretail.feign.model.OrderDetailResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient("base-order-service-v1-0")
public interface OrderService {

    //创建订单
    @RequestMapping(value = "/orders/", method = RequestMethod.POST)
    ResponseEntity<OrderDetailResult> createOrder(@RequestBody CreateOrderParam orderParam);

    //修改订单状态为已发货
    @RequestMapping(value = "/orders/{orderCode}/delivered", method = RequestMethod.PUT)
    ResponseEntity delivered(@PathVariable("orderCode") String orderCode);

    //处理订单状态配送中
    @RequestMapping(value = "/orders/{orderCode}/dispatching", method = RequestMethod.PUT)
    ResponseEntity dispatching(@PathVariable("orderCode") String orderCode);

    //修改订单为已收货
    @RequestMapping(value = "/orders/{orderCode}/received", method = RequestMethod.PUT)
    ResponseEntity received(@PathVariable("orderCode") String orderCode);


    @RequestMapping(value = "/orders/{orderCode}", method = RequestMethod.GET)
    ResponseEntity<OrderDetailResult> orderDetail(@PathVariable("orderCode") String orderCode, @RequestParam("needProductList") boolean needProductList,
                               @RequestParam("needOrderFlowList") boolean needOrderFlowList);

}
