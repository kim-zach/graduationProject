package com.kimi.kel.core.pojo.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Video对象", description="")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
      @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "上传视频作者")
    private Long userId;

    @ApiModelProperty(value = "上传视频id")
    private String videoSourceId;

    private Integer viewAmount;

    private Integer clickAmount;

    private Integer collectAmount;

    private String title;

    private String brief;

    @ApiModelProperty(value = "0:未审核；2：审核不通过，1：审核通过")
    private Integer status;

    private LocalDateTime createTime;

    private String tag;

    private LocalDateTime updateTime;

    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
