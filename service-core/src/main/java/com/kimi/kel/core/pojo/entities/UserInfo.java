package com.kimi.kel.core.pojo.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author kimi
 * @since 2022-02-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserInfo对象", description="")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String mobile;

    private String password;

    private String nickName;

    private String name;

    private String email;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "微信用户标识openid")
    private String openid;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "状态 ，（0：锁定，1：正常）")
    private Integer status;

    @ApiModelProperty(value = "wordGather排名")
    private Integer rank;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除（0:未删除，1:删除）")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
