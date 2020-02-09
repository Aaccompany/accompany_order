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



    @Override
    public User getCurUser(){
        return SecurityInterceptor.loginUser.get();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delUser(Long userId) {
        User curUser = getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        User user = getById(userId);
        if (user==null){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("用户不存在"));
        }
        user.setIsDel(true);
        saveOrUpdate(user);
    }

    @Override
    public void updateUser(Long userId, UserReqVo userReqVo) {
        User curUser = getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        User user = userMapper.selectById(userId);
        if (user==null){
            throw new BaseRuntimeException(ResultCode.PARAM_ILLEGAL.modifyMessage("用户不存在"));
        }
        BeanUtils.copyProperties(userReqVo,user);
        saveOrUpdate(user);
    }

    @Override
    public Page<User> findAll(String name, int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> query = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            query.lambda()
                .like(User::getName,"%"+name+"%");
        }
        return userMapper.selectPage(page, query);
    }

    @Override
    public User login(UserLoginVo loginVo) {
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

    @Override
    public void addUser(UserReqVo userReqVo) {
        User curUser = getCurUser();
        if (curUser==null||!curUser.getIsSuperAdmin()){
            throw new BaseRuntimeException(ResultCode.PERMISSION_DENIED.modifyMessage("权限不足"));
        }
        User user = CommonUtils.genByCopyProperties(userReqVo, User.class);
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setIsDel(false);
        save(user);
    }
}
