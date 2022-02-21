package com.kimi.kel.core.pojo.vo;

import lombok.Data;

@Data
public class VideoInfoVO {

    private String title;
    private String brief;
    private String tag;

    private int clickAmount;
    private int viewAmount;
    private int collectAmount;
    private int status;

    private Long userId;
    private String videoSourceId;
}
