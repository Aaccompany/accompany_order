package com.accompany.order.controller.user;


import com.accompany.order.config.SecurityInterceptor;
import com.accompany.order.exception.BaseRuntimeException;
import com.accompany.order.service.user.dao.IUserService;
import com.accompany.order.service.user.dto.User;
import com.accompany.order.util.CommonUtils;
import com.accompany.order.util.Result;
import com.accompany.order.util.ResultCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@RestController
@RequestMapping("/user")
@Api(tags = {"所有接口"})
public class UserController {

    @Resource(name = "userServiceImpl")
    private IUserService userService;

    @Autowired
    private HttpSession session;

    @Autowired
    private Gson gson;

    @PostMapping("/")
    @ApiOperation(value = "新增管理员(完成)", tags = "管理员->人员管理")
    public Result addUser(@RequestBody UserReqVo userReqVo) {
        userService.addUser(userReqVo);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除管理员(完成)", tags = "管理员->人员管理")
    public Result addUser(@PathVariable Long id) {
        userService.delUser(id);
        return Result.success();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改管理员(完成)", tags = "管理员->人员管理")
    public Result updateUser(@PathVariable Long id, @RequestBody UserReqVo userReqVo) {
        userService.updateUser(id, userReqVo);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "管理员详情(完成)", tags = "管理员->人员管理")
    public Result<UserResVo> userDetail(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(CommonUtils.genByCopyProperties(user, UserResVo.class));
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询全部管理员(完成)", tags = {"管理员->人员管理"})
    @ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "管理员名称 模糊查询"),
        @ApiImplicitParam(name = "pageNum", value = "当前页默认为1"),
        @ApiImplicitParam(name = "pageSize", value = "当前页默认为20"),
    })
    public Result<Page<UserResVo>> findAll(@RequestParam(required = false) String name, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "20") int pageSize) {
        Page<User> userPage = userService.findAll(name, pageNum, pageSize);
        return Result.success(CommonUtils.genPageByCopyProperties(userPage, UserResVo.class));
    }

    @PostMapping("/login")
    @ApiOperation(value = "管理员登入(完成)", tags = {"管理员->人员管理"})
    public Result<UserResVo> login(@RequestBody UserLoginVo loginVo) {
        User user = userService.login(loginVo);
        session.setAttribute(SecurityInterceptor.SESSION_USER, gson.toJson(user));
        UserResVo userResVo = CommonUtils.genByCopyProperties(user, UserResVo.class);
        userResVo.setToken("Admin");
        userResVo.setAvatar("http://huyaimg.msstatic.com/avatar/1010/6a/d86b613c53b470efe58bf8d47bd40a_180_135.jpg");
        userResVo.setRoles(Lists.newArrayList(new String[]{"Admin"}));
        return Result.success(userResVo);
    }

    @GetMapping("/info")
    @ApiOperation(value = "管理员信息", tags = {"管理员->人员管理"})
    public Result<UserResVo> loginInfo() {
        User user = SecurityInterceptor.loginUser.get();
        if (user == null) {
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("登入后重试"));
        }
        UserResVo userResVo = CommonUtils.genByCopyProperties(user, UserResVo.class);
        userResVo.setToken("Admin");
        userResVo.setAvatar("http://huyaimg.msstatic.com/avatar/1010/6a/d86b613c53b470efe58bf8d47bd40a_180_135.jpg");
        userResVo.setRoles(Lists.newArrayList(new String[]{"Admin"}));
        return Result.success(userResVo);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "管理员退出", tags = {"管理员->人员管理"})
    public Result logout() {
        User user = SecurityInterceptor.loginUser.get();
        if (user == null) {
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("登入后重试"));
        }
        SecurityInterceptor.loginUser.remove();
        session.removeAttribute(SecurityInterceptor.SESSION_USER);
        return Result.success();
    }
}

