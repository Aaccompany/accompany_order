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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @Autowired
    private OrderMapper orderMapper;

    @Resource(name = "footServiceImpl")
    private IFootService footService;

    @Resource(name = "orderItemServiceImpl")
    private IOrderItemService orderItemService;

    @Override
    public void payOrder(Long orderId) {
        User curUser = userService.getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        Order order = orderMapper.selectById(orderId);
        if (order==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("订单不存在"));
        }
        order.setIsPay(1);
        order.setUpdateTime(new Date());
        order.setUpdateUser(curUser.getName());
        saveOrUpdate(order);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void placeOrder(OrderReqVo orderReqVo) {
        Date now = new Date();
        Order order = CommonUtils.genByCopyProperties(orderReqVo, Order.class);
        order.setIsPay(0);
        order.setUpdateTime(now);
        order.setCreateTime(now);
        orderMapper.insert(order);
        for (OrderItemReqVo itemReqVo : orderReqVo.getItmeList()) {
            Foot foot = footService.getById(itemReqVo.getFootId());
            OrderItem item = CommonUtils.genByCopyProperties(itemReqVo,OrderItem.class);
            item.setOrderId(order.getId());
            item.setCreateTime(now);
            item.setUpdateTime(now);
            item.setMoney(itemReqVo.getCount()*foot.getMoney());
            item.setFootName(foot.getName());
            item.setUnitPrice(foot.getMoney());
            orderItemService.save(item);
        }
    }

    @Override
    public Order orderDetail(Long orderId) {
        User curUser = userService.getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        Order order = orderMapper.selectById(orderId);
        if (order==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("订单不存在"));
        }
        return order;
    }

    @Override
    public Order findByTableId(int tableId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(Order::getTableId,tableId)
            .eq(Order::getIsPay,0);
        Order order ;
        try {
            order = orderMapper.selectOne(queryWrapper);
        }catch (Exception e){
            throw new BaseRuntimeException(ResultCode.OPERATOR_UNSUPPORT.modifyMessage("该桌没有付费，请通知店员处理"));
        }
        return order;
    }

    @Override
    public Page<Order> findAllByQuery(int isPay, int pageNum, int pageSize) {
        User curUser = userService.getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(Order::getIsPay,isPay);
        Page<Order> page = new Page<>(pageNum,pageSize);
        queryWrapper.orderByDesc("update_time");
        return orderMapper.selectPage(page,queryWrapper);
    }

    /**
     *
     * @param startTime 如果不传递则为一天内的订单
     * @param endTime 如果不传入则为从开始时间到当前时间的订单
     * @param isPay 表示某种方式结算 1:表示结算 0：表示未结算 不填则查询全部
     * @return 符合时间的订单列表
     */
    @Override
    public List<Order> findAllByQuery(Long startTime, Long endTime,int isPay) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getIsPay,isPay);
        if (startTime!=null&&startTime!=0){
            queryWrapper.ge(Order::getCreateTime,new Date(startTime));
        }
        if (endTime!=null&&endTime!=0){
            queryWrapper.le(Order::getCreateTime,new Date(endTime));
        }
        return orderMapper.selectList(queryWrapper);
    }

}
