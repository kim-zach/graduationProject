package com.kimi.kel.core.pojo.vo;

import lombok.Data;

@Data
public class CommentVO {

    private Long id;

    private Long userId;

    private Long videoId;

    private String content;

    private String userName;

    private String avatar;
}
