package com.kimi.kel.core.service;

import com.kimi.kel.core.pojo.entities.Administrator;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kimi
 * @since 2022-01-23
 */
public interface AdministratorService extends IService<Administrator> {

    boolean ifExists(String name,String password);
}
