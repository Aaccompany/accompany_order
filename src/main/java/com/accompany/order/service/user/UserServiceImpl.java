package com.accompany.order.service.user;

import com.accompany.order.config.SecurityInterceptor;
import com.accompany.order.controller.user.UserLoginVo;
import com.accompany.order.controller.user.UserReqVo;
import com.accompany.order.exception.BaseRuntimeException;
import com.accompany.order.service.user.dao.IUserService;
import com.accompany.order.service.user.dao.UserMapper;
import com.accompany.order.service.user.dto.User;
import com.accompany.order.util.CommonUtils;
import com.accompany.order.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取当前登入对象信息，针对后台服务提供
     * @return 当前登入对象信息
     */
    @Override
    public User getCurUser(){
        return SecurityInterceptor.loginUser.get();
    }

    /**
     * 删除管理员
     * @param userId 管理员id
     */
    @Override
    public void delUser(Long userId) {
        //获取当前登入用户信息
        User curUser = getCurUser();
        //判断当前登入是否具操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //获取需要删除的用户是否存在
        User user = getById(userId);
        if (user==null){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("用户不存在"));
        }
        user.setIsDel(true);
        //删除管理员
        saveOrUpdate(user);
    }

    /**
     * 修改管理员信息
     * @param userId 需要修改的管理员id
     * @param userReqVo 需要修改的管理员信息
     */
    @Override
    public void updateUser(Long userId, UserReqVo userReqVo) {
        //获取当前登入用户信息
        User curUser = getCurUser();
        //判断当前登入是否具操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        //获取需要修改的用户是否存在
        User user = getById(userId);
        if (user==null){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("用户不存在"));
        }
        BeanUtils.copyProperties(userReqVo,user);
        //修改用户信息
        saveOrUpdate(user);
    }

    /**
     * 查询管理员分页列表
     * @param name 模糊查询管理员名称
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @return 分页对象
     */
    @Override
    public Page<User> findAll(String name, int pageNum, int pageSize) {
        //查询全部管理员
        Page<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> query = new QueryWrapper<>();
        query.orderByAsc("is_del");
        if (!StringUtils.isEmpty(name)){
            query.lambda()
                .like(User::getName,"%"+name+"%");
        }
        return userMapper.selectPage(page, query);
    }

    /**
     * 管理员登入
     * @param loginVo 登入信息对象
     * @return 登入后的管理员信息
     */
    @Override
    public User login(UserLoginVo loginVo) {
        //通过用户输入的账号密码获取管理员对象
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda()
            .eq(User::getUsername,loginVo.getUsername())
            .eq(User::getPassword,loginVo.getPassword());
        User user = userMapper.selectOne(query);
        if (user==null){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("用户不存在"));
        }
        return user;
    }

    /**
     * 添加管理员
     * @param userReqVo 管理员基本信息
     */
    @Override
    public void addUser(UserReqVo userReqVo) {
        //获取当前登入用户信息
        User curUser = getCurUser();
        //判断当前登入是否具操作权限
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        User user = CommonUtils.genByCopyProperties(userReqVo, User.class);
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setIsDel(false);
        //保存管理员信息
        save(user);
    }
}
