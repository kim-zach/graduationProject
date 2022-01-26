package com.kimi.kel.core.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.kimi.kel.core.convertor.LocalDateTimeConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelInitialWordDTO implements Serializable {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("parent_id")
    private Long parentId;

    @ExcelProperty("word_spell")
    private String wordSpell;

    @ExcelProperty("description")
    private String description;

    @ExcelProperty("tag")
    private String tag;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "create time")
    @ExcelProperty(value = "create_time",converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "update time")
    @ExcelProperty(value = "update_time",converter = LocalDateTimeConverter.class)
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getWordSpell() {
        return wordSpell;
    }

    public void setWordSpell(String wordSpell) {
        this.wordSpell = wordSpell;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
