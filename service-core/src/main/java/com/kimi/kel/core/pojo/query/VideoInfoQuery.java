package com.kimi.kel.core.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "视频搜索对象")
public class VideoInfoQuery {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "审核状态")
    private Integer status;

    private String tag;

    private String userId;


}
