package com.accompany.order.controller.foot;


import com.accompany.order.controller.foot.foottypevo.FootTypeAdminReqVo;
import com.accompany.order.controller.foot.foottypevo.FootTypeAdminResVo;
import com.accompany.order.controller.foot.foottypevo.FootTypeResVo;
import com.accompany.order.controller.foot.footvo.FootAdminReqVo;
import com.accompany.order.controller.foot.footvo.FootAdminResVo;
import com.accompany.order.controller.foot.footvo.FootResVo;
import com.accompany.order.exception.BaseRuntimeException;
import com.accompany.order.service.foot.dao.IFootService;
import com.accompany.order.service.foot.dto.Foot;
import com.accompany.order.service.footType.dao.IFootTypeService;
import com.accompany.order.service.footType.dto.FootType;
import com.accompany.order.util.CommonUtils;
import com.accompany.order.util.Result;
import com.accompany.order.util.ResultCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@RestController
@RequestMapping("/foot")
@Api(tags = {"所有接口"})
public class FootController {
    @Resource(name = "footServiceImpl")
    private IFootService footService;

    @Resource(name = "footTypeServiceImpl")
    private IFootTypeService footTypeService;

    @GetMapping("/")
    @ApiOperation(value = "查询全部菜品(完成)",tags = {"用户页面"})
    public Result<List<FootResVo>> findAllFoot(){
        List<Foot> list = footService.findAllFoot();
        return Result.success(CommonUtils.genListByCopyProperties(list,FootResVo.class));
    }

    @GetMapping("/footType")
    @ApiOperation(value = "查询全部菜品类别(完成)",tags = {"用户页面"})
    public Result<List<FootTypeResVo>> findAllFootType(){
        List<FootType> list = footTypeService.findAllFootType();
        return Result.success(CommonUtils.genListByCopyProperties(list,FootTypeResVo.class));
    }


    @GetMapping("/admin")
    @ApiOperation(value = "查询全部菜品(完成)",tags = {"管理员->菜品管理"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name",value = "菜品名称 模糊查询"),
        @ApiImplicitParam(name = "pageNum",value = "当前页默认为1"),
        @ApiImplicitParam(name = "pageSize",value = "当前页默认为20"),
    })
    public Result<Page<FootAdminResVo>> findAllFootAdmin(@RequestParam String name, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "20") int pageSize){
        Page<Foot> list = footService.findAllFootAdmin(name,pageNum,pageSize);
        return Result.success(CommonUtils.genPageByCopyProperties(list,FootAdminResVo.class));
    }

    @PostMapping("/admin")
    @ApiOperation(value = "新增菜品(完成)",tags = {"管理员->菜品管理"})
    public Result addFootAdmin(@RequestBody FootAdminReqVo footAdminReqVo){
        footService.addFoot(footAdminReqVo);
        return Result.success();
    }

    @DeleteMapping("/admin/{id}")
    @ApiOperation(value = "删除菜品(完成)",tags = {"管理员->菜品管理"})
    @ApiImplicitParam(name = "id",value = "菜品id")
    public Result delFootAdmin(@PathVariable Long id){
        footService.delFoot(id);
        return Result.success();
    }

    @GetMapping("/admin/{id}")
    @ApiOperation(value = "菜品详情(完成)",tags = {"管理员->菜品管理"})
    @ApiImplicitParam(name = "id",value = "菜品id")
    public Result<FootAdminResVo> footDetailAdmin(@PathVariable Long id){
        Foot foot = footService.getById(id);
        if (foot==null){
            throw new  BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("菜品不存在"));
        }
        return Result.success(CommonUtils.genByCopyProperties(foot,FootAdminResVo.class));
    }

    @PutMapping("/admin/{id}")
    @ApiOperation(value = "修改菜品(完成)",tags = {"管理员->菜品管理"})
    @ApiImplicitParam(name = "id",value = "菜品id")
    public Result updateFootAdmin(@PathVariable Long id,@RequestBody FootAdminReqVo footAdminReqVo){
        footService.updateFoot(id,footAdminReqVo);
        return Result.success();
    }

    @GetMapping("/admin/footType")
    @ApiOperation(value = "查询全部菜品类别(完成)",tags = {"管理员->菜品类别管理"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name",value = "类别名称 模糊查询"),
        @ApiImplicitParam(name = "pageNum",value = "当前页默认为1"),
        @ApiImplicitParam(name = "pageSize",value = "当前页默认为20"),
    })
    public Result<Page<FootAdminResVo>> findAllFootTypeAdmin(@RequestParam String name,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "20") int pageSize){
        Page<FootType> page = footTypeService.findAllFootType(name, pageNum, pageSize);
        return Result.success(CommonUtils.genPageByCopyProperties(page,FootAdminResVo.class));
    }

    @PostMapping("/admin/footType")
    @ApiOperation(value = "新增菜品类别(完成)",tags = {"管理员->菜品类别管理"})
    public Result addFootTypeAdmin(@RequestBody FootTypeAdminReqVo footTypeAdminReqVo){
        footTypeService.addFootType(footTypeAdminReqVo);
        return Result.success();
    }

    @DeleteMapping("/admin/footType/{id}")
    @ApiOperation(value = "删除菜品类别(完成)",tags = {"管理员->菜品类别管理"})
    @ApiImplicitParam(name = "id",value = "类别id")
    public Result delFootTypeAdmin(@PathVariable Long id){
        footTypeService.delFootType(id);
        return Result.success();
    }

    @GetMapping("/admin/footType/{id}")
    @ApiOperation(value = "菜品详情类别(完成)",tags = {"管理员->菜品类别管理","用户页面"})
    @ApiImplicitParam(name = "id",value = "类别id")
    public Result<FootTypeAdminResVo> footTypeDetailAdmin(@PathVariable Long id){
        FootType footType = footTypeService.footTypeDetail(id);
        return Result.success(CommonUtils.genByCopyProperties(footType,FootTypeAdminResVo.class));
    }

    @PutMapping("/admin/footType/{id}")
    @ApiOperation(value = "修改菜品类别",tags = {"管理员->菜品类别管理"})
    @ApiImplicitParam(name = "id",value = "类别id")
    public Result updateFootTypeAdmin(@PathVariable Long id,@RequestBody FootTypeAdminReqVo footTypeAdminReqVo){
        footTypeService.updateFootType(id,footTypeAdminReqVo);
        return Result.success();
    }
}

