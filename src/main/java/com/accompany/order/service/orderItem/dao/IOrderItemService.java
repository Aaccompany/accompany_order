package com.accompany.order.service.orderItem.dao;

import com.accompany.order.service.orderItem.dto.OrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
public interface IOrderItemService extends IService<OrderItem> {

    List<OrderItem> findAllByOrderId(Long orderId);
}
