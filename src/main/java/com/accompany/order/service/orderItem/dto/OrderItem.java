package com.accompany.order.service.orderItem.dto;

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
@TableName("order_item")
@ApiModel(value="OrderItem对象", description="")
public class OrderItem implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单项id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品id")
    @TableField("foot_id")
    private Long footId;

    @ApiModelProperty(value = "父订单id")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty(value = "菜品名称")
    @TableField("foot_name")
    private String footName;

    @ApiModelProperty(value = "商品总数")
    @TableField("count")
    private Integer count;

    @ApiModelProperty(value = "订单项总价")
    @TableField("money")
    private Double money;

    @ApiModelProperty(value = "商品单价")
    @TableField("unit_price")
    private Double unitPrice;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("update_time")
    private Date updateTime;


}
