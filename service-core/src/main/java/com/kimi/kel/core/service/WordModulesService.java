package com.kimi.kel.core.service;

import com.kimi.kel.core.pojo.entities.WordModules;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kimi
 * @since 2022-01-26
 */
public interface WordModulesService extends IService<WordModules> {

    boolean ifExistModule(String tag);

    List<String> getModules();

}
