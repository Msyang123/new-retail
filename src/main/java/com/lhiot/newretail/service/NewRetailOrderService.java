package com.lhiot.newretail.service;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.Calculator;
import com.leon.microx.util.StringUtils;
import com.leon.microx.web.result.Pages;
import com.leon.microx.web.result.Tips;
import com.lhiot.newretail.config.NewRetailFreegoConfig;
import com.lhiot.newretail.feign.BaseDataService;
import com.lhiot.newretail.feign.DeliverService;
import com.lhiot.newretail.feign.HaidingService;
import com.lhiot.newretail.feign.OrderService;
import com.lhiot.newretail.feign.model.*;
import com.lhiot.newretail.feign.type.*;
import com.lhiot.newretail.mapper.NewRetailOrderMapper;
import com.lhiot.newretail.mapper.OrderProductMapper;
import com.lhiot.newretail.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class NewRetailOrderService {

    private NewRetailOrderMapper newRetailOrderMapper;
    private OrderProductMapper orderProductMapper;

    private HaidingService haidingService;
    private DeliverService deliverService;
    private OrderService orderService;
    private BaseDataService baseDataService;

    private NewRetailFreegoConfig.OrderConfig orderConfig;
    private NewRetailFreegoConfig.DeliverConfig deliverConfig;
    private RestTemplate restTemplate;

    public NewRetailOrderService(NewRetailOrderMapper newRetailOrderMapper,
                                 OrderProductMapper orderProductMapper, HaidingService haidingService,
                                 DeliverService deliverService,
                                 OrderService orderService,
                                 BaseDataService baseDataService, NewRetailFreegoConfig newRetailFreegoConfig,
                                 RestTemplate restTemplate) {
        this.newRetailOrderMapper = newRetailOrderMapper;
        this.orderProductMapper = orderProductMapper;
        this.haidingService = haidingService;
        this.deliverService = deliverService;
        this.orderService = orderService;
        this.baseDataService = baseDataService;
        this.orderConfig = newRetailFreegoConfig.getOrder();
        this.deliverConfig = newRetailFreegoConfig.getDeliver();
        this.restTemplate = restTemplate;
    }

    public Tips createOrder(NewRetailOrder newRetailOrder) {
        //防止第三方重复调用，幂等处理
        if(StringUtils.isNotBlank(newRetailOrder.getPartnerCode())){
            //依据合作伙伴订单号判断是否已经存在
            NewRetailOrder searchNewRetailOrder = newRetailOrderMapper.orderDetailByPartnerCode(newRetailOrder.getPartnerCode());
            if (Objects.nonNull(searchNewRetailOrder)) {
                log.warn("重复发送{}",searchNewRetailOrder);
                return Tips.of(1, String.format("{\"hdOrderCode\":%s}", searchNewRetailOrder.getOrderCode()));
            }
        }
        //校验库存是否足够
        Object[] barCodeArr = newRetailOrder.getOrderProducts().parallelStream().map(NewRetailOrderProduct::getBarcode).toArray(String[]::new);
        String barCodes = StringUtils.join(",", barCodeArr);
        ResponseEntity<Map<String, Object>> querySkuResponse = haidingService.querySku(newRetailOrder.getOrderStore().getStoreCode(), newRetailOrder.getOrderProducts().parallelStream().map(NewRetailOrderProduct::getBarcode).toArray(String[]::new));

        List<Map> businvs = (List<Map>) querySkuResponse.getBody().get("businvs");

        ProductSpecificationParam productSpecificationParam = new ProductSpecificationParam();
        productSpecificationParam.setBarCodes(barCodes);
        productSpecificationParam.setAvailableStatus(AvailableStatus.ENABLE);
        ResponseEntity<Pages<ProductSpecification>> pagesResponseEntity = baseDataService.search(productSpecificationParam);
        if (Objects.isNull(pagesResponseEntity) || pagesResponseEntity.getStatusCode().isError()) {
            return Tips.of(HttpStatus.BAD_REQUEST, "查询基础服务商品规格错误");
        }
        Pages<ProductSpecification> productSpecificationPages = pagesResponseEntity.getBody();
        for (ProductSpecification productSpecification : productSpecificationPages.getArray()) {
            for (Map businv : businvs) {
                if (Objects.equals(productSpecification.getBarcode(), businv.get("barCode"))
                        && productSpecification.getLimitInventory() > Double.valueOf(businv.get("qty").toString())) {
                    return Tips.of(HttpStatus.BAD_REQUEST,
                            String.format("%s实际库存%s低于安全库存%d，不允许销售", productSpecification.getBarcode(), businv.get("qty").toString(), productSpecification.getLimitInventory()));
                }
            }
        }

        //给商品赋值规格数量
        newRetailOrder.getOrderProducts().forEach(product ->
            productSpecificationPages.getArray().stream()
                .filter(productSpecification -> Objects.equals(product.getBarcode(), productSpecification.getBarcode()))
                .forEach(item -> {
                    product.setSpecificationId(item.getId());//设置规格id
                    //product.setSpecificationQty(item.getSpecificationQty());
                    product.setSpecificationQty(BigDecimal.ONE);
                    product.setTotalWeight(item.getWeight());
                })
        );

        CreateOrderParam createOrderParam = new CreateOrderParam();
        BeanUtils.copyProperties(newRetailOrder, createOrderParam);

        List<OrderProduct> orderProducts = new ArrayList<>(newRetailOrder.getOrderProducts().size());
        newRetailOrder.getOrderProducts().forEach(newRetailOrderProduct -> {
            OrderProduct orderProduct = new OrderProduct();
            BeanUtils.copyProperties(newRetailOrderProduct, orderProduct);
            orderProduct.setProductQty(newRetailOrderProduct.getProductQty().intValue());
            orderProduct.setShelfQty(BigDecimal.ONE);
            newRetailOrderProduct.setSpecificationQty(BigDecimal.ONE);
            orderProducts.add(orderProduct);
        });
        createOrderParam.setOrderProducts(orderProducts);

        OrderStore orderStore = new OrderStore();

        ResponseEntity<Store> storeResponseEntity = baseDataService.findStoreByCode(newRetailOrder.getOrderStore().getStoreCode(),ApplicationType.APP);
        if(Objects.isNull(storeResponseEntity)||storeResponseEntity.getStatusCode().isError()){
            log.error("创建订单查询基础服务门店信息失败,门店编码{}",newRetailOrder.getOrderStore().getStoreCode());
            orderStore.setStoreId(-1L);
        }else{
            orderStore.setStoreId(storeResponseEntity.getBody().getId());
        }
        orderStore.setStoreCode(newRetailOrder.getOrderStore().getStoreCode());
        orderStore.setStoreName(newRetailOrder.getOrderStore().getStoreName());
        orderStore.setOperationUser(newRetailOrder.getOrderStore().getOperationUser());
        createOrderParam.setOrderStore(orderStore);
        createOrderParam.setUserId(9999L);//由于没有用户信息 故默认
        createOrderParam.setApplicationType(ApplicationType.APP);
        createOrderParam.setOrderType("FREEGO");
        createOrderParam.setAllowRefund(AllowRefund.YES);//是否订单允许退款
        //发送基础服务创建订单
        ResponseEntity<OrderDetailResult> responseEntity = orderService.createOrder(createOrderParam);

        if (Objects.nonNull(responseEntity) && responseEntity.getStatusCode().is2xxSuccessful()) {
            OrderDetailResult orderDetailResult = responseEntity.getBody();

            //保存本地订单数据
            newRetailOrder.setOrderCode(orderDetailResult.getCode());
            newRetailOrderMapper.createOrder(newRetailOrder);
            //保存订单商品
            newRetailOrder.getOrderProducts().parallelStream().forEach(item -> item.setOrderCode(orderDetailResult.getCode()));
            orderProductMapper.batchInsert(newRetailOrder.getOrderProducts());
            //推送到海鼎
            HdOrderInfo hdOrderInfo = new HdOrderInfo();
            BeanUtils.copyProperties(newRetailOrder, hdOrderInfo);
            hdOrderInfo.setApplyType(ApplicationType.APP);
            hdOrderInfo.setCode(newRetailOrder.getOrderCode());
            hdOrderInfo.setHdOrderCode(newRetailOrder.getOrderCode());
            hdOrderInfo.setStoreCode(newRetailOrder.getOrderStore().getStoreCode());
            hdOrderInfo.setStoreName(newRetailOrder.getOrderStore().getStoreName());
            hdOrderInfo.setOrderStatus(OrderStatus.WAIT_SEND_OUT.name());
            hdOrderInfo.setReceivingWay(newRetailOrder.getReceivingWay().name());
            hdOrderInfo.setUserId(9999L);
            hdOrderInfo.setRemark(ApplicationType.APP.getDescription()+"-"+newRetailOrder.getRemark());
            hdOrderInfo.setPayAt(Date.from(Instant.now()));
            ResponseEntity<String> haidingReduceResponse = haidingService.reduce(hdOrderInfo);
            if (Objects.nonNull(haidingReduceResponse) && haidingReduceResponse.getStatusCode().is2xxSuccessful()) {
                //发送海鼎订单成功
                log.info("发送海鼎订单成功:{}", orderDetailResult.getCode());
                //设置基础服务为待发货状态，因为此处创建的订单已经支付完成
                orderService.updateOrderStatus(orderDetailResult.getCode(),OrderStatus.WAIT_SEND_OUT);
            } else {
                //TODO 重试或者后台管理系统处理
                log.error("发送海鼎订单错误:{}", orderDetailResult.getCode());
            }
            return Tips.of(1, String.format("{\"hdOrderCode\":%s}", orderDetailResult.getCode()));
        }
        return Tips.of(HttpStatus.BAD_REQUEST, String.valueOf(responseEntity.getBody()));
    }

    public String  batchCancel(String[] orderCodes,String reason){
        StringBuffer result = new StringBuffer();
        for (String orderCode : orderCodes){
            ResponseEntity responseEntity = haidingService.cancel(orderCode,reason);
            if(Objects.isNull(responseEntity) || responseEntity.getStatusCode().isError()){
                result.append(orderCode)
                        .append(",");
            }
            log.info("取消海鼎订单返回结果:{}",responseEntity);
        }
        return result.toString();
    }

    public void batchPush(String[] orderCodes){
        for(String orderCode: orderCodes){
            NewRetailOrder newRetailOrder = newRetailOrderMapper.orderDetailByCode(orderCode);
            //推送到海鼎
            HdOrderInfo hdOrderInfo = new HdOrderInfo();
            BeanUtils.copyProperties(newRetailOrder, hdOrderInfo);
            hdOrderInfo.setApplyType(ApplicationType.APP);
            hdOrderInfo.setCode(newRetailOrder.getOrderCode());
            hdOrderInfo.setHdOrderCode(newRetailOrder.getOrderCode());
            hdOrderInfo.setStoreCode(newRetailOrder.getOrderStore().getStoreCode());
            hdOrderInfo.setStoreName(newRetailOrder.getOrderStore().getStoreName());
            hdOrderInfo.setOrderStatus(OrderStatus.WAIT_SEND_OUT.name());
            hdOrderInfo.setReceivingWay(newRetailOrder.getReceivingWay().name());
            hdOrderInfo.setUserId(9999L);
            hdOrderInfo.setRemark(ApplicationType.APP.getDescription()+"-"+newRetailOrder.getRemark());
            ResponseEntity<String> haidingReduceResponse = haidingService.reduce(hdOrderInfo);
            log.info(haidingReduceResponse.getBody());
        }
    }

    //推送线上订单到新零售门店 （非海鼎系统）
    public Tips pushOrderToStore(NewRetailOrder newRetailOrder) {

        //构造新零售门店订单信息并发送
        FreeGoOrderRequest freeGoOrderRequest = new FreeGoOrderRequest();
        freeGoOrderRequest.setOrderCode(newRetailOrder.getOrderCode());
        freeGoOrderRequest.setShopId(newRetailOrder.getOrderStore().getStoreCode());
        freeGoOrderRequest.setShopName(newRetailOrder.getOrderStore().getStoreName());
        freeGoOrderRequest.setOrderType(Objects.equals(newRetailOrder.getReceivingWay(), ReceivingWay.TO_THE_STORE) ? "1001" : "1002");//必填（1001自提 1002 配送）
        freeGoOrderRequest.setRecrivedName(newRetailOrder.getReceiveUser());
        freeGoOrderRequest.setRecrivedPhone(newRetailOrder.getContactPhone());
        freeGoOrderRequest.setRecrivedAddress(newRetailOrder.getAddress());
        freeGoOrderRequest.setRemark("绿航");
        freeGoOrderRequest.setTotalPrice(newRetailOrder.getTotalAmount());

        //设置订单商品信息
        List<FreeGoOrder> freeGoOrders = new ArrayList<>(newRetailOrder.getOrderProducts().size());
        newRetailOrder.getOrderProducts().forEach(item -> {
            FreeGoOrder freeGoOrder = new FreeGoOrder();
            freeGoOrder.setBarcode(item.getBarcode());
            freeGoOrder.setNum(item.getProductQty().intValue());
            freeGoOrder.setPrice(item.getTotalPrice());
            freeGoOrders.add(freeGoOrder);
        });
        freeGoOrderRequest.setOrders(freeGoOrders);

        ResponseEntity<FreeGoOrderResponse> orderResponse = restTemplate.postForEntity(String.format("%s/lvhang/receiveLvHangOrders", orderConfig.getBaseUrl()),
                freeGoOrderRequest,
                FreeGoOrderResponse.class);

        return Objects.nonNull(orderResponse) && orderResponse.getStatusCode().is2xxSuccessful() ?
                Tips.of(HttpStatus.OK, "推送订单到门店成功") : Tips.of(HttpStatus.BAD_REQUEST, "推送订单到门店失败");
    }

    /**
     * 新零售门店订单出门店
     */
    public Tips orderOutOfStore(String orderCode) {
        //查询订单信息
        ResponseEntity<OrderDetailResult> orderDetailResultResponseEntity = orderService.orderDetail(orderCode, true, false);
        if (Objects.isNull(orderDetailResultResponseEntity)
                || orderDetailResultResponseEntity.getStatusCode().isError()) {
            return Tips.of(HttpStatus.BAD_REQUEST, String.valueOf(orderDetailResultResponseEntity.getBody()));
        }
        OrderDetailResult orderDetailResult = orderDetailResultResponseEntity.getBody();
        if (Objects.equals(orderDetailResult.getReceivingWay(), ReceivingWay.TO_THE_STORE)) {
            //门店自提订单 直接结束
            ResponseEntity receivedResponseEntity = orderService.updateOrderStatus(orderCode,OrderStatus.RECEIVED);
            if (Objects.isNull(receivedResponseEntity) || receivedResponseEntity.getStatusCode().isError()) {
                return Tips.of(HttpStatus.BAD_REQUEST, "调用订单状态收货失败");
            }
            return Tips.of(HttpStatus.OK, String.valueOf(receivedResponseEntity.getBody()));
        } else {
            //出门店的配送单，订单配送中
            /*ResponseEntity dispatchingResponseEntity = orderService.updateOrderStatus(orderCode,OrderStatus.DISPATCHING);
            if (Objects.isNull(dispatchingResponseEntity) || dispatchingResponseEntity.getStatusCode().isError()) {
                return Tips.of(HttpStatus.BAD_REQUEST, "调用订单修改为配送中失败");
            }
            return Tips.of(HttpStatus.OK, String.valueOf(dispatchingResponseEntity.getBody()));
            */
            return Tips.of(HttpStatus.OK, "配送骑手已出门店，订单状态交由配送流程流转");
        }
    }

    public NewRetailOrder getOrderByOrderCode(String orderCode) {
        NewRetailOrder newRetailOrder = newRetailOrderMapper.orderDetailByCode(orderCode);
        if (Objects.nonNull(newRetailOrder)) {
            newRetailOrder.setOrderProducts(orderProductMapper.listByOrderCode(orderCode));
        }
        return newRetailOrder;
    }

    //处理海鼎回调
    public Tips hdCallbackDeal(@RequestBody Map<String, Object> map) {
        Map<String, String> contentMap = (Map<String, String>)map.get("content");

        log.info("content = " + contentMap.toString());
        String orderCode = contentMap.get("front_order_id");
        //查询订单信息
        ResponseEntity<OrderDetailResult> orderDetailResultResponseEntity = orderService.orderDetail(orderCode, true, false);
        if (Objects.isNull(orderDetailResultResponseEntity)
                || orderDetailResultResponseEntity.getStatusCode().isError()) {
            return Tips.of(HttpStatus.BAD_REQUEST, String.valueOf(orderDetailResultResponseEntity.getBody()));
        }
        OrderDetailResult orderDetailResult = orderDetailResultResponseEntity.getBody();
        // 所有订单推送类消息
        if ("order".equals(map.get("group"))) {
            // 订单备货
            if ("order.shipped".equals(map.get("topic"))) {
                log.info("订单备货回调********");
                if(!Objects.equals(orderDetailResult.getStatus(),OrderStatus.WAIT_SEND_OUT)){
                    //如果已经处理此订单信息，就不重复处理
                    return Tips.of(HttpStatus.OK, orderCode);
                }
                //如果送货上门 发送订单到配送中心

                //门店自提 此处不做处理，等待孚利购推送订单出店接口数据再修改订单完成
                if (Objects.equals(orderDetailResult.getReceivingWay(), ReceivingWay.TO_THE_STORE)) {
                    //调用基础服务修改为已发货状态baseUrl
                    orderService.updateOrderStatus(orderDetailResult.getCode(),OrderStatus.RECEIVED);
                } else {
                    //发送到配送(达达)
                    DeliverOrder deliverOrder = new DeliverOrder();
                    deliverOrder.setAddress(orderDetailResult.getAddress());
                    deliverOrder.setAmountPayable(orderDetailResult.getAmountPayable());
                    deliverOrder.setApplyType(ApplicationType.APP);
                    deliverOrder.setBackUrl(deliverConfig.getBackUrl());//配置回调
                    deliverOrder.setContactPhone(orderDetailResult.getContactPhone());
                    deliverOrder.setCouponAmount(orderDetailResult.getCouponAmount());

                    ZoneId zoneId = ZoneId.systemDefault();
                    LocalDateTime current = LocalDateTime.now();
                    ZonedDateTime zdt = current.atZone(zoneId);
                    Date dateStart = Date.from(zdt.toInstant());

                    deliverOrder.setCreateAt(dateStart);

                    current.plusHours(1);//加一个小时
                    Date dateEnd = Date.from(zdt.toInstant());
                    deliverOrder.setDeliverTime(DeliverTime.of("立即配送", dateStart, dateEnd));
                    deliverOrder.setDeliveryFee(orderDetailResult.getDeliveryAmount());
                    deliverOrder.setHdOrderCode(orderDetailResult.getHdOrderCode());
                    ResponseEntity<Store> storeResponseEntity = baseDataService.findStoreByCode(orderDetailResult.getOrderStore().getStoreCode(),ApplicationType.APP);
                    if (Objects.nonNull(storeResponseEntity) && storeResponseEntity.getStatusCode().is2xxSuccessful()) {
                        deliverOrder.setLat(storeResponseEntity.getBody().getLatitude().doubleValue());//TODO 配送经纬度不是门店的经纬度，是收货地址的经纬度
                        deliverOrder.setLng(storeResponseEntity.getBody().getLongitude().doubleValue());
                    } else {
                        log.error("查询门店信息失败", orderDetailResult.getOrderStore().getStoreCode());
                        deliverOrder.setLat(0.00);
                        deliverOrder.setLng(0.00);
                    }
                    deliverOrder.setOrderId(orderDetailResult.getId());
                    deliverOrder.setOrderCode(orderDetailResult.getCode());
                    deliverOrder.setReceiveUser(orderDetailResult.getReceiveUser());
                    deliverOrder.setRemark(orderDetailResult.getRemark());
                    deliverOrder.setStoreCode(orderDetailResult.getOrderStore().getStoreCode());
                    deliverOrder.setStoreName(orderDetailResult.getOrderStore().getStoreName());
                    deliverOrder.setTotalAmount(orderDetailResult.getTotalAmount());
                    deliverOrder.setUserId(orderDetailResult.getUserId());

                    List<DeliverProduct> deliverProductList = new ArrayList<>(orderDetailResult.getOrderProductList().size());
                    orderDetailResult.getOrderProductList().forEach(item -> {
                        DeliverProduct deliverProduct = new DeliverProduct();
                        deliverProduct.setBarcode(item.getBarcode());
                        deliverProduct.setBaseWeight(item.getTotalWeight().doubleValue());
                        deliverProduct.setDeliverBaseOrderId(orderDetailResult.getId());
                        deliverProduct.setDiscountPrice(item.getDiscountPrice());
                        deliverProduct.setImage(item.getImage());
                        deliverProduct.setLargeImage(item.getImage());
                        deliverProduct.setPrice(item.getTotalPrice());
                        deliverProduct.setProductName(item.getProductName());
                        deliverProduct.setProductQty(item.getProductQty());
                        deliverProduct.setSmallImage(item.getImage());
                        deliverProduct.setStandardPrice((int) Calculator.div(item.getTotalPrice(), item.getProductQty()));
                        deliverProduct.setStandardQty(Double.valueOf(item.getShelfQty().toString()));
                        deliverProductList.add(deliverProduct);
                    });
                    deliverOrder.setDeliverOrderProductList(deliverProductList);//填充订单商品

                    //发送达达配送
                    ResponseEntity<Tips> deliverResponseEntity = deliverService.create(DeliverType.valueOf(deliverConfig.getType()), CoordinateSystem.AMAP, deliverOrder);

                    if (Objects.nonNull(deliverResponseEntity) && deliverResponseEntity.getStatusCode().is2xxSuccessful()) {
                        //设置成已发货
                        ResponseEntity sendOutResponse = orderService.updateOrderStatus(orderDetailResult.getCode(),OrderStatus.SEND_OUT);
                        if (Objects.nonNull(sendOutResponse) && sendOutResponse.getStatusCode().is2xxSuccessful()) {
                            log.info("调用基础服务修改为已发货状态正常{}",orderCode);
                        } else {
                            log.error("调用基础服务修改为已发货状态错误{}",orderCode);
                        }
                    }
                }
                return Tips.of(HttpStatus.OK, orderCode);
            } else if ("return.received".equals(map.get("topic"))) {
                log.info("订单退货回调*********");
                if (Objects.equals(OrderStatus.RETURNING, orderDetailResult.getStatus())) {
                    log.info("给用户退款", orderDetailResult);
                    //退款
                    //判断退款是否成功
                    //改订单为退款成功
                }
            } else {
                log.info("hd other group message= " + map.get("group"));
            }
        }
        return Tips.of(HttpStatus.BAD_REQUEST, String.valueOf(orderDetailResultResponseEntity.getBody()));
    }

    public Tips deliverCallbackDeal(Map<String, Object> param) {
        //修改配送单状态 如果配送完成 修改订单状态为已完成
        Map<String, String> stringParams = Optional.ofNullable(param).map(
            (v) -> {
                Map<String, String> params = v.entrySet().stream()
                        .filter((e) -> StringUtils.isNotEmpty(e.getValue()))
                        .collect(Collectors.toMap(
                                (e) -> e.getKey(),
                                (e) -> Objects.isNull(e.getValue())?null:e.getValue().toString()
                        ));
                return params;
            }
        ).orElse(null);
        //验证签名
        ResponseEntity<Tips> backSignature = deliverService.backSignature(DeliverType.valueOf(deliverConfig.getType()), stringParams);

        if (Objects.nonNull(backSignature) && backSignature.getStatusCode().is2xxSuccessful()) {
            log.debug("配送签名回调验证结果:{}", backSignature.getBody());
            String orderCode = stringParams.get("order_id");

            switch ((int) param.get("order_status")) {
                case 1:
                    deliverService.update(orderCode, new DeliverUpdate(orderCode, DeliverStatus.UNRECEIVE, null, null, null));
                    return Tips.of(HttpStatus.OK, "配送待接单");
                case 2:
                    //调用基础服务修改为配送中状态
                    orderService.updateOrderStatus(orderCode,OrderStatus.DISPATCHING);
                    deliverService.update(orderCode, new DeliverUpdate(orderCode, DeliverStatus.WAIT_GET, stringParams.get("dm_name"), stringParams.get("dm_mobile"), null));
                    return Tips.of(HttpStatus.OK, "配送待取货");
                case 3:
                    deliverService.update(orderCode, new DeliverUpdate(orderCode, DeliverStatus.DELIVERING, null, null, null));
                    return Tips.of(HttpStatus.OK, "配送配送中");
                case 4:
                    deliverService.update(orderCode, new DeliverUpdate(orderCode, DeliverStatus.DONE, null, null, null));
                    orderService.updateOrderStatus(orderCode,OrderStatus.RECEIVED);
                    return Tips.of(HttpStatus.OK, "配送配送完成");
                case 5:
                    deliverService.update(orderCode, new DeliverUpdate(orderCode, DeliverStatus.FAILURE, null, null, "已取消"));
                    return Tips.of(HttpStatus.OK, "配送已取消");
                case 7:
                    deliverService.update(orderCode, new DeliverUpdate(orderCode, DeliverStatus.FAILURE, null, null, "已过期"));
                    return Tips.of(HttpStatus.OK, "配送已过期");
                case 1000:
                    deliverService.update(orderCode, new DeliverUpdate(orderCode, DeliverStatus.FAILURE, null, null, stringParams.get("cancel_reason")));
                    return Tips.of(HttpStatus.OK, "配送失败");
                default:
                    break;
            }
        } else {
            log.error("配送回调验证签名不通过");
        }
        //直接不作处理
        return Tips.of(HttpStatus.OK, "默认处理");
    }
}
