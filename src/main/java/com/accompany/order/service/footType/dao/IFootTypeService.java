package com.accompany.order.service.footType.dao;

import com.accompany.order.controller.foot.foottypevo.FootTypeAdminReqVo;
import com.accompany.order.service.footType.dto.FootType;
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
public interface IFootTypeService extends IService<FootType> {

    List<FootType> findAllFootType();

    Page<FootType> findAllFootType(String name, int pageNum, int pageSize);

    void addFootType(FootTypeAdminReqVo footTypeAdminReqVo);

    void delFootType(Long id);

    FootType footTypeDetail(Long id);

    void updateFootType(Long id, FootTypeAdminReqVo footTypeAdminReqVo);
}
