package com.kimi.kel.core.pojo.vo;

import com.kimi.kel.core.pojo.entities.VideoComment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VideoDetailsVO {

    @ApiModelProperty(value = "上传视频作者")
    private Long userId;

    @ApiModelProperty(value = "上传视频作者名字")
    private String userName;

    @ApiModelProperty(value = "上传视频云id")
    private String videoSourceId;

//    @ApiModelProperty(value = "上传视频id")
//    private String videoSourceId;

    private Integer viewAmount;

    private Integer collectAmount;

    private Integer clickAmount;

    private String title;

    private String brief;

    @ApiModelProperty(value = "0:未审核：2：审核不通过，1：审核通过")
    private Integer status;

    private LocalDateTime createTime;

    private String tag;

    //播放凭证
    private String playAuth;

    //评论
    List<VideoComment> comments;
}
