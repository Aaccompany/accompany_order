package com.accompany.order.service.orderItem;

import com.accompany.order.service.orderItem.dto.OrderItem;
import com.accompany.order.service.orderItem.dao.OrderItemMapper;
import com.accompany.order.service.orderItem.dao.IOrderItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    /**
     * 根据订单id查询全部订单项
     * @param orderId 订单id
     * @return 订单下的全部订单项
     */
    @Override
    public List<OrderItem> findAllByOrderId(Long orderId) {
        //查询{orderId}订单下的全部订单项
        QueryWrapper<OrderItem> query = new QueryWrapper<>();
        query.lambda()
            .eq(OrderItem::getOrderId,orderId);
        List<OrderItem> itemList = orderItemMapper.selectList(query);
        return itemList==null? Lists.newArrayList():itemList;
    }
}
