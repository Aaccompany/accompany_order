package com.accompany.order.controller.shop;


import com.accompany.order.service.shop.dao.IShopService;
import com.accompany.order.util.CommonUtils;
import com.accompany.order.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@RestController
@RequestMapping("/shop")
@Api(tags = {"所有接口"})
public class ShopController {
    @Resource(name = "shopServiceImpl")
    private IShopService shopService;

    @GetMapping("/{id}")
    @ApiOperation(value = "获取店面信息(完成)",tags = "用户页面")
    @ApiImplicitParam(name = "id",value = "店面id")
    public Result<ShopResVo> findShopMes(@PathVariable Long id){
        return Result.success(CommonUtils.genByCopyProperties(shopService.findById(id),ShopResVo.class));
    }
}

