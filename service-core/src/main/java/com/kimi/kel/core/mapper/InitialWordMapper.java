package com.kimi.kel.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kimi.kel.core.pojo.dto.ExcelInitialWordDTO;
import com.kimi.kel.core.pojo.entities.DefaultWord;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author kimi
 * @since 2021-08-10
 */
public interface InitialWordMapper extends BaseMapper<DefaultWord> {

    void insertBatch(List<ExcelInitialWordDTO> list);
}
