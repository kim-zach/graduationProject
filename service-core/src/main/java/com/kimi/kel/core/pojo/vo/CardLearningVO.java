package com.kimi.kel.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "卡片记单词对象")
public class CardLearningVO {

    //用来存单词本
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "单词")
    private String wordSpell;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "标签")
    private String tag;


}
