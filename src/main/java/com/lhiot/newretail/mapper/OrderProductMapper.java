package com.lhiot.newretail.mapper;

import com.lhiot.newretail.model.NewRetailOrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 数据库处理新零售订单类
* @author yijun
* @date 2018/07/24
*/
@Mapper
@Repository
public interface OrderProductMapper {

    /**
    * Description:创建新零售订单
    *
    * @param orderProducts
    * @return
    * @author yijun
    * @date 2018/07/24 09:55:48
    */
    int batchInsert(List<NewRetailOrderProduct> orderProducts);

    List<NewRetailOrderProduct> listByOrderCode(String orderCode);

}
