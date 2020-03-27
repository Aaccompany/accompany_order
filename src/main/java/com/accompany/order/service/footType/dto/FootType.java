package com.accompany.order.service.footType.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Accompany
 * @since 2019-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("foot_type")
@ApiModel(value="FootType对象", description="")
public class FootType implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "类别id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "类别名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "类别描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "是否删除 1:表示删除 0：未删除")
    @TableField("is_del")
    private Boolean isDel;

    @ApiModelProperty(value = "创建者")
    @TableField("create_user")
    private Long createUser;

    @ApiModelProperty(value = "修改者")
    @TableField("update_user")
    private Long updateUser;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;


}
