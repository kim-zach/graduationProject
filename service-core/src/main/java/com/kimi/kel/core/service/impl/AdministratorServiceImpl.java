package com.kimi.kel.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kimi.kel.core.pojo.entities.Administrator;
import com.kimi.kel.core.mapper.AdministratorMapper;
import com.kimi.kel.core.service.AdministratorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kimi
 * @since 2022-01-23
 */
@Service
public class AdministratorServiceImpl extends ServiceImpl<AdministratorMapper, Administrator> implements AdministratorService {

    @Override
    public boolean ifExists(String name, String password) {
        QueryWrapper<Administrator> administratorQueryWrapper = new QueryWrapper<>();
        administratorQueryWrapper.eq("user_name",name).eq("password",password);
        Administrator administrator = baseMapper.selectOne(administratorQueryWrapper);
        if (administrator != null) {
            return true;
        }
        return false;

    }
}
