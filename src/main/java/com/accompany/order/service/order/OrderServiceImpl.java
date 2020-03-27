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

    /**
     * 支付订单
     * @param orderId 需要支付的订单id
     */
    @Override
    public void payOrder(Long orderId) {
        //获取当前登入用户信息
        User curUser = userService.getCurUser();
        //判断当前登入是否具操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //获取订单信息，并且判断订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order==null){
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
     * @param orderReqVo 需要创建的订单信息
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void placeOrder(OrderReqVo orderReqVo) {
        Date now = new Date();
        //初始化订单
        Order order = CommonUtils.genByCopyProperties(orderReqVo, Order.class);
        order.setIsPay(0);
        order.setUpdateTime(now);
        order.setCreateTime(now);
        double money =0;
        //获取全部菜品
        Map<Long,Integer> footId2Count = orderReqVo.getItmeList().stream().collect(Collectors.toMap(OrderItemReqVo::getFootId,OrderItemReqVo::getCount));
        //通过菜品Id查询菜品集合
        List<Foot> footList = footService.findByIds(Lists.newArrayList(footId2Count.keySet()));
        Map<Long, Foot> footMap = footList.stream().collect(Collectors.toMap(Foot::getId, item -> item));
        for (Long footId : footId2Count.keySet()) {
            Integer count = footId2Count.get(footId);
            Foot foot = footMap.get(footId);
            money = money +(count*foot.getMoney());
        }
        order.setMoney(money);
        saveOrUpdate(order);
        //添加订单中的每一个订单项到数据库
        for (OrderItemReqVo itemReqVo : orderReqVo.getItmeList()) {
            Foot foot = footMap.get(itemReqVo.getFootId());
            //初始化订单项
            OrderItem item = CommonUtils.genByCopyProperties(itemReqVo,OrderItem.class);
            item.setOrderId(order.getId());
            item.setCreateTime(now);
            item.setUpdateTime(now);
            double itemMoney = itemReqVo.getCount() * foot.getMoney();
            item.setMoney(itemMoney);
            item.setFootName(foot.getName());
            item.setUnitPrice(foot.getMoney());
            //把订单项保存到数据库
            orderItemService.save(item);
        }

    }

    /**
     * 订单详情
     * @param orderId 需要查询的订单id
     * @return 订单对象
     */
    @Override
    public Order orderDetail(Long orderId) {
        //获取当前登入用户信息
        User curUser = userService.getCurUser();
        //判断当前登入是否具操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //获取订单信息，并且判断订单是否存在
        Order order = orderMapper.selectById(orderId);
        if (order==null){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("订单不存在"));
        }
        return order;
    }

    /**
     * 通过桌号查询未支付订单
     * @param tableId 桌号
     * @return 订单对象
     */
    @Override
    public Order findByTableId(int tableId) {
        //过桌号查询未支付订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
            .eq(Order::getTableId,tableId)
            .eq(Order::getIsPay,0);
        List<Order> orderList = orderMapper.selectList(queryWrapper);
        if (orderList.size()>1){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("系统异常,该桌子之前的订单未付款"));
        }else if (orderList.size()==0){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("该卓还未下单，下单后重试"));
        }
        return orderList.get(0);
    }
    /**
     * 查询订单分页列表
     * @param isPay 订单是否支付 0：没有支付 1：支付
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @return 分页对象
     */
    @Override
    public Page<Order> findAllByQuery(int tableId,int isPay, int pageNum, int pageSize) {
        //获取当前登入用户信息
        User curUser = userService.getCurUser();
        //判断当前登入是否具操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //根据条件分页查询
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (isPay!=-1){
            queryWrapper.lambda()
                .eq(Order::getIsPay,isPay);
        }
        if (tableId!=-1){
            queryWrapper.lambda()
                .eq(Order::getTableId,tableId);
        }
        Page<Order> page = new Page<>(pageNum,pageSize);
        //根据更新时间对订单进行降序排序
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
