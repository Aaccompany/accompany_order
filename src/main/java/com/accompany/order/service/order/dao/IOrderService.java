package com.accompany.order.service.order.dao;

import com.accompany.order.controller.order.OrderReqVo;
import com.accompany.order.service.order.dto.Order;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface IOrderService extends IService<Order> {

    void payOrder(Long id);

    void placeOrder(OrderReqVo orderReqVo);

    Order orderDetail(Long id);

    Order findByTableId(int tableId);

    Page<Order> findAllByQuery(int tableId,int isPay, int pageNum, int pageSize);

    List<Order> findAllByQuery(Long startTime, Long endTime,int isPay);
}
