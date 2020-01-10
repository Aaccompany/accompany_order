package com.accompany.order.service.foot.dao;

import com.accompany.order.controller.foot.footvo.FootAdminReqVo;
import com.accompany.order.service.foot.dto.Foot;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
public interface IFootService extends IService<Foot> {

    List<Foot> findAllFoot();

    Page<Foot> findAllFootAdmin(String name, int pageNum, int pageSize);

    void addFoot(FootAdminReqVo footAdminReqVo);

    void delFoot(Long footId);

    void updateFoot(Long id, FootAdminReqVo footAdminReqVo);
}
