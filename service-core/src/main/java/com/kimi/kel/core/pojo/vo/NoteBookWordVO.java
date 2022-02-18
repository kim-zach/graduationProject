package com.kimi.kel.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiModel(description = "返回给前端的生词对象")
public class NoteBookWordVO {

    private Long userId;
    private String wordSpell;
    private String description;
    private String tag;
}
