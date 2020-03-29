package com.accompany.order.service.order;

import com.accompany.order.controller.order.OrderAdminResVo;
import com.accompany.order.controller.order.OrderItemAdminResVo;
import com.accompany.order.controller.order.OrderItemReqVo;
import com.accompany.order.controller.order.OrderReqVo;
import com.accompany.order.exception.BaseRuntimeException;
import com.accompany.order.service.foot.dao.IFootService;
import com.accompany.order.service.foot.dto.Foot;
import com.accompany.order.service.order.dao.IOrderService;
import com.accompany.order.service.order.dao.OrderMapper;
import com.accompany.order.service.order.dto.Order;
import com.accompany.order.service.orderItem.dao.IOrderItemService;
import com.accompany.order.service.orderItem.dto.OrderItem;
import com.accompany.order.service.user.dao.IUserService;
import com.accompany.order.service.user.dto.User;
import com.accompany.order.util.CommonUtils;
import com.accompany.order.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @Resource
    private OrderMapper orderMapper;

    @Resource(name = "footServiceImpl")
    private IFootService footService;

    @Resource(name = "orderItemServiceImpl")
    private IOrderItemService orderItemService;

    /**
     * 支付订单
     *
     * @param orderId 需要支付的订单id
     */
    @Override
    public void payOrder(Long orderId) {
        //获取当前登入用户信息
        User curUser = userService.getCurUser();
        //判断当前登入是否具操作权限
        if (curUser == null || !curUser.getIsSuperAdmin()) {
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //获取订单信息，并且判断订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("订单不存在"));
        }
        order.setIsPay(1);
        order.setUpdateTime(new Date());
        order.setUpdateUser(curUser.getId());
        //修改订单状态
        saveOrUpdate(order);
    }

    /**
     * 创建订单
     * 需要加锁
     * @param orderReqVo 需要创建的订单信息
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void placeOrder(OrderReqVo orderReqVo) {
        //通过菜品Id查询菜品集合
        Map<Long, Integer> footId2Count = orderReqVo.getItmeList().stream().collect(Collectors.toMap(OrderItemReqVo::getFootId, OrderItemReqVo::getCount));
        List<Foot> footList = footService.findByIds(Lists.newArrayList(footId2Count.keySet()));
        Map<Long, Foot> footMap = footList.stream().collect(Collectors.toMap(Foot::getId, item -> item));
        Double orderMoney = orderSum(footId2Count, footMap);
        //查询桌号是否之前下过单
        Order order = findByTableId(orderReqVo.getTableId());
        if (order != null) {
            updateOrder(orderReqVo, order, orderMoney, footMap, footId2Count);
            return;
        }
        addOrder(orderReqVo, orderMoney, footMap);
    }

    /**
     * 修改订单
     */
    public void updateOrder(OrderReqVo updateOrder, Order order, double orderMoney, Map<Long, Foot> footMap, Map<Long, Integer> footId2Count) {
        Date now = new Date();
        //表示是加餐操作,将新添加的菜品添加到该订单的订单项
        order.setUpdateTime(now);
        order.setMoney(order.getMoney() + orderMoney);
        //保存订单
        saveOrUpdate(order);
        //查询添加的菜品是否在之前已经添加过了，如果添加过了就需要在原来的基础上添加
        //根据订单号+菜品id查询
        List<OrderItem> orderItems = orderItemService.findAllByOrderIdAndFootIds(order.getId(), Lists.newArrayList(footId2Count.keySet()));
        Map<Long, OrderItem> oldFootId2OrderItem = orderItems.stream().collect(Collectors.toMap(OrderItem::getFootId, item->item));
        //根据用户加餐的订单项来更新数据库中的订单项或新增订单项
        for (OrderItemReqVo orderItemReqVo : updateOrder.getItmeList()) {
            OrderItem orderItem = oldFootId2OrderItem.get(orderItemReqVo.getFootId());
            Foot foot = footMap.get(orderItemReqVo.getFootId());
            if (orderItem==null){
                //表示为新的菜品
                orderItemService.saveOrderItem(Lists.newArrayList(orderItemReqVo),footMap,order);
                continue;
            }
            //表示顾客之前已经下单过了,更新订单项
            orderItem.setCount(orderItemReqVo.getCount());
            double orderItemMoney = orderItem.getMoney() + foot.getMoney()*orderItem.getCount();
            orderItem.setMoney(orderItemMoney);
            orderItemService.updateById(orderItem);
        }
    }

    /**
     * 添加订单
     */
    public void addOrder(OrderReqVo orderReqVo, double orderMoney, Map<Long, Foot> footMap) {
        //初始化订单
        Date now = new Date();
        Order order = CommonUtils.genByCopyProperties(orderReqVo, Order.class);
        order.setIsPay(0);
        order.setUpdateTime(now);
        order.setCreateTime(now);
        order.setMoney(orderMoney);
        saveOrUpdate(order);
        orderItemService.saveOrderItem(orderReqVo.getItmeList(),footMap,order);
    }

    public Double orderSum(Map<Long, Integer> footId2Count, Map<Long, Foot> footMap) {
        double money = 0;
        for (Long footId : footId2Count.keySet()) {
            Integer count = footId2Count.get(footId);
            Foot foot = footMap.get(footId);
            money = money + (count * foot.getMoney());
        }
        return money;
    }

    /**
     * 订单详情
     *
     * @param orderId 需要查询的订单id
     * @return 订单对象
     */
    @Override
    public Order orderDetail(Long orderId) {
        //获取当前登入用户信息
        User curUser = userService.getCurUser();
        //判断当前登入是否具操作权限
        if (curUser == null || !curUser.getIsSuperAdmin()) {
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //获取订单信息，并且判断订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("订单不存在"));
        }
        return order;
    }

    /**
     * 通过桌号查询未支付订单
     *
     * @param tableId 桌号
     * @return 订单对象
     */
    @Override
    public Order findByTableId(int tableId) {
        //过桌号查询未支付订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(Order::getTableId, tableId)
            .eq(Order::getIsPay, 0);
        List<Order> orderList = orderMapper.selectList(queryWrapper);
        if (orderList.size() > 1) {
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("系统异常,该桌子之前的订单未付款"));
        } else if (orderList.size() == 0) {
            //throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("该卓还未下单，下单后重试"));
            return null;
        }
        return orderList.get(0);
    }

    /**
     * 查询订单分页列表
     *
     * @param isPay    订单是否支付 0：没有支付 1：支付
     * @param pageNum  当前页
     * @param pageSize 页面大小
     * @return 分页对象
     */
    @Override
    public Page<Order> findAllByQuery(int tableId, int isPay, int pageNum, int pageSize) {
        //获取当前登入用户信息
        User curUser = userService.getCurUser();
        //判断当前登入是否具操作权限
        if (curUser == null || !curUser.getIsSuperAdmin()) {
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //根据条件分页查询
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (isPay != -1) {
            queryWrapper.lambda()
                .eq(Order::getIsPay, isPay);
        }
        if (tableId != -1) {
            queryWrapper.lambda()
                .eq(Order::getTableId, tableId);
        }
        Page<Order> page = new Page<>(pageNum, pageSize);
        //根据更新时间对订单进行降序排序
        queryWrapper.orderByDesc("update_time");
        return orderMapper.selectPage(page, queryWrapper);
    }

    /**
     * @param startTime 如果不传递则为一天内的订单
     * @param endTime   如果不传入则为从开始时间到当前时间的订单
     * @param isPay     表示某种方式结算 1:表示结算 0：表示未结算 不填则查询全部
     * @return 符合时间的订单列表
     */
    @Override
    public List<Order> findAllByQuery(Long startTime, Long endTime, int isPay) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getIsPay, isPay);
        if (startTime != null && startTime != 0) {
            queryWrapper.ge(Order::getCreateTime, new Date(startTime));
        }
        if (endTime != null && endTime != 0) {
            queryWrapper.le(Order::getCreateTime, new Date(endTime));
        }
        return orderMapper.selectList(queryWrapper);
    }

}
