package com.accompany.order.service.orderItem.dao;

import com.accompany.order.controller.order.OrderItemReqVo;
import com.accompany.order.service.foot.dto.Foot;
import com.accompany.order.service.order.dto.Order;
import com.accompany.order.service.orderItem.dto.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
public interface IOrderItemService extends IService<OrderItem> {

    List<OrderItem> findAllByOrderId(Long orderId);

    List<OrderItem> findAllByOrderIdAndFootIds(Long orderId, List<Long> footIds);

    void saveOrderItem(List<OrderItemReqVo> orderItemList, Map<Long, Foot> footMap , Order order);
}
