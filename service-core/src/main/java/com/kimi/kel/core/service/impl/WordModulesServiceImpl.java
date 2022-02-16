package com.kimi.kel.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kimi.kel.core.pojo.entities.WordModules;
import com.kimi.kel.core.mapper.WordModulesMapper;
import com.kimi.kel.core.service.WordModulesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kimi
 * @since 2022-01-26
 */
@Service
public class WordModulesServiceImpl extends ServiceImpl<WordModulesMapper, WordModules> implements WordModulesService {

    @Resource
    private WordModulesMapper wordModulesMapper;

    @Override
    public boolean ifExistModule(String tag) {
        QueryWrapper<WordModules> wordModulesQueryWrapper = new QueryWrapper<>();
        wordModulesQueryWrapper.eq("tag",tag);
        com.kimi.kel.core.pojo.entities.WordModules one = baseMapper.selectOne(wordModulesQueryWrapper);
        if(one != null){
            return true;
        }
        return false;
    }

    @Override
    public List<String> getModules() {
        QueryWrapper<WordModules> wordModulesQueryWrapper = new QueryWrapper<>();
        wordModulesQueryWrapper.select("tag");

        List<String> modules = wordModulesMapper.getTags();

        return modules;
    }
}
