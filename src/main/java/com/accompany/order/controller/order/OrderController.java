package com.accompany.order.controller.order;


import com.accompany.order.service.order.dao.IOrderService;
import com.accompany.order.service.order.dto.Order;
import com.accompany.order.service.orderItem.dao.IOrderItemService;
import com.accompany.order.service.orderItem.dto.OrderItem;
import com.accompany.order.util.CommonUtils;
import com.accompany.order.util.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@RestController
@RequestMapping("/order")
@Api(tags = {"所有接口"})
public class OrderController {

    @Resource(name = "orderServiceImpl")
    private IOrderService orderService;

    @Resource(name = "orderItemServiceImpl")
    private IOrderItemService orderItemService;

    @PutMapping("/admin/pay/{id}")
    @ApiOperation(value = "支付下单(完成)",tags = {"管理员->订单管理"})
    @ApiImplicitParam(name = "id",value = "订单id")
    public Result payOrder(@PathVariable Long id){
        orderService.payOrder(id);
        return Result.success();
    }

    @PostMapping("/")
    @ApiOperation(value = "用户下单(完成)",tags = {"用户页面"})
    public Result placeOrder(@RequestBody OrderReqVo orderReqVo){
        orderService.placeOrder(orderReqVo);
        return Result.success();
    }

    @GetMapping("/admin/{id}")
    @ApiOperation(value = "订单详情(完成)",tags = {"管理员->订单管理"})
    @ApiImplicitParam(name = "id",value = "订单id")
    public Result<OrderAdminResVo> orderDetail(@PathVariable Long id){
        Order order = orderService.orderDetail(id);
        return Result.success(genOrderDetailVo(order));
    }

    @GetMapping("/admin/table/{id}")
    @ApiOperation(value = "桌号订单详情(完成)",tags = {"管理员->订单管理"})
    @ApiImplicitParam(name = "id",value = "桌号id")
    public Result<OrderAdminResVo> tableOrderDetail(@PathVariable int id){
        Order order = orderService.findByTableId(id);
        return Result.success(genOrderDetailVo(order));
    }

    @GetMapping("/admin/list")
    @ApiOperation(value = "订单列表(完成)",tags = {"管理员->订单管理"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "tableId",value = "查询对应桌号信息，不填则查询全部"),
        @ApiImplicitParam(name = "isPay",value = "表示某种方式结算 1:表示结算 0：表示未结算 不填则查询全部"),
        @ApiImplicitParam(name = "pageNum",value = "当前页默认为1"),
        @ApiImplicitParam(name = "pageSize",value = "当前页默认为20")
    })
    public Result<Page<OrderAdminResVo>> placeOrder(@RequestParam(defaultValue = "-1") int tableId,@RequestParam(defaultValue = "-1") int isPay,@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "20") int pageSize){
        Page<Order> page = orderService.findAllByQuery(tableId,isPay,pageNum,pageSize);
        return Result.success(CommonUtils.genPageByCopyProperties(page,OrderAdminResVo.class));
    }

    @GetMapping("/admin/profit")
    @ApiOperation(value = "店家收入只查询已支付的(完成)",tags = {"管理员->收入明细"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime",value = "如果不传递则为一天内的收入"),
        @ApiImplicitParam(name = "endTime",value = "如果不传入则为从开始时间到当前时间的收入"),
    })
    public Result<Double> profit( Long startTime, Long endTime){
        List<Order> orderList = orderService.findAllByQuery(startTime,endTime,1);
        double profit = orderList.stream().mapToDouble(Order::getMoney).sum();
        return Result.success(profit);
    }


    /**
     * 构建订单ListVo
     */
    private Page<OrderAdminResVo> genOrdersDetailVo(Page<Order> orderPage){
        Page<OrderAdminResVo> orderAdminResVoPage = CommonUtils.genPageByCopyProperties(orderPage, OrderAdminResVo.class);
        for (OrderAdminResVo orderAdminResVo : orderAdminResVoPage.getRecords()) {
            List<OrderItem> orderItemList = orderItemService.findAllByOrderId(orderAdminResVo.getId());
            List<OrderItemAdminResVo> orderItemAdminResVos = CommonUtils.genListByCopyProperties(orderItemList, OrderItemAdminResVo.class);
            orderAdminResVo.setItemList(orderItemAdminResVos);
        }
        return orderAdminResVoPage;
    }

    /**
     * 构建订单Vo
     */
    private OrderAdminResVo genOrderDetailVo(Order order){
        List<OrderItem> orderItemList = orderItemService.findAllByOrderId(order.getId());
        OrderAdminResVo orderAdminResVo = CommonUtils.genByCopyProperties(order, OrderAdminResVo.class);
        List<OrderItemAdminResVo> orderItemAdminResVos = CommonUtils.genListByCopyProperties(orderItemList, OrderItemAdminResVo.class);
        orderAdminResVo.setItemList(orderItemAdminResVos);
        return orderAdminResVo;
    }

}

