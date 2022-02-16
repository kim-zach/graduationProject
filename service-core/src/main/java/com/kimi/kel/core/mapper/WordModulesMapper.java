package com.kimi.kel.core.mapper;

import com.kimi.kel.core.pojo.entities.WordModules;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kimi
 * @since 2022-01-26
 */
public interface WordModulesMapper extends BaseMapper<WordModules> {

    List<String> getTags();
}
