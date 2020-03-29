package com.accompany.order.service.orderItem;

import com.accompany.order.controller.order.OrderItemReqVo;
import com.accompany.order.service.foot.dto.Foot;
import com.accompany.order.service.order.dto.Order;
import com.accompany.order.service.orderItem.dto.OrderItem;
import com.accompany.order.service.orderItem.dao.OrderItemMapper;
import com.accompany.order.service.orderItem.dao.IOrderItemService;
import com.accompany.order.util.CommonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
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
     *
     * @param orderId 订单id
     * @return 订单下的全部订单项
     */
    @Override
    public List<OrderItem> findAllByOrderId(Long orderId) {
        //查询{orderId}订单下的全部订单项
        QueryWrapper<OrderItem> query = new QueryWrapper<>();
        query.lambda()
            .eq(OrderItem::getOrderId, orderId);
        List<OrderItem> itemList = orderItemMapper.selectList(query);
        return itemList == null ? Lists.newArrayList() : itemList;
    }

    @Override
    public List<OrderItem> findAllByOrderIdAndFootIds(Long orderId, List<Long> footIds) {
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(OrderItem::getOrderId, orderId)
            .in(OrderItem::getFootId, footIds);
        List<OrderItem> list = orderItemMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(list) ? Lists.newArrayList() : list;
    }

    @Override
    public void saveOrderItem(List<OrderItemReqVo> orderItemList, Map<Long, Foot> footMap, Order order) {
        Date now = new Date();
        //添加订单中的每一个订单项到数据库
        for (OrderItemReqVo itemReqVo : orderItemList) {
            Foot foot = footMap.get(itemReqVo.getFootId());
            //初始化订单项
            OrderItem item = CommonUtils.genByCopyProperties(itemReqVo, OrderItem.class);
            item.setOrderId(order.getId());
            item.setCreateTime(now);
            item.setUpdateTime(now);
            double itemMoney = itemReqVo.getCount() * foot.getMoney();
            item.setMoney(itemMoney);
            item.setFootName(foot.getName());
            item.setUnitPrice(foot.getMoney());
            //把订单项保存到数据库
            save(item);
        }
    }
}
