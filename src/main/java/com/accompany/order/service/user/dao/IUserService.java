package com.accompany.order.service.user.dao;

import com.accompany.order.controller.user.UserLoginVo;
import com.accompany.order.controller.user.UserReqVo;
import com.accompany.order.service.user.dto.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
public interface IUserService extends IService<User> {
    void addUser(UserReqVo userReqVo);

    User getCurUser();

    void delUser(Long id);

    void updateUser(Long id, UserReqVo userReqVo);

    Page<User> findAll(String name, int pageNum, int pageSize);

    void login(UserLoginVo loginVo);
}
