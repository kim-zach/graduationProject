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
 * @since 2022-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DefaultWord对象", description="")
public class DefaultWord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long parentId;

    @ApiModelProperty(value = "name of the word ")
    private String wordSpell;

    @ApiModelProperty(value = "description")
    private String description;

    @ApiModelProperty(value = "what kind of word")
    private String tag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


    @TableField(exist = false) //表达逻辑概念的属性，和物理表没有关系，当前字段不存在物理表中
    private boolean hasChildren;
}
