package com.lhiot.newretail.mapper;

import com.lhiot.newretail.model.NewRetailOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* 数据库处理新零售订单类
* @author yijun
* @date 2018/07/24
*/
@Mapper
@Repository
public interface NewRetailOrderMapper {

    /**
    * Description:创建新零售订单
    *
    * @param newRetailOrder
    * @return
    * @author yijun
    * @date 2018/07/24 09:55:48
    */
    int createOrder(NewRetailOrder newRetailOrder);

    /**
     * 查询订单详情
     * @param orderCode
     * @return
     */
    NewRetailOrder orderDetailByCode(String orderCode);

    /**
     * 依据合作者订单号查询订单详情
     * @param partnerCode
     * @return
     */
    NewRetailOrder orderDetailByPartnerCode(String partnerCode);

}
