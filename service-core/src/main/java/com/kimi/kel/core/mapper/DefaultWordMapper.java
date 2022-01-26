package com.kimi.kel.core.mapper;

import com.kimi.kel.core.pojo.dto.ExcelInitialWordDTO;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kimi
 * @since 2022-01-23
 */
public interface DefaultWordMapper extends BaseMapper<DefaultWord> {

    void insertBatch(List<ExcelInitialWordDTO> list);

    void updateBatch(List<ExcelInitialWordDTO> list);

    Set<String>  selectWordSpell();

}
