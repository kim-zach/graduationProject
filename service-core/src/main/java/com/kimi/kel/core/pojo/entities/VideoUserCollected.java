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
 * @since 2022-02-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="VideoUserCollected对象", description="")
public class VideoUserCollected implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long videoId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
