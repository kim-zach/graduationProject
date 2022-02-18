package com.kimi.kel.core.pojo.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordVocabularyNotebook {

    private Long id;
    private Long userId;
    private String wordSpell;
    private String description;
    private String tag;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;
}
