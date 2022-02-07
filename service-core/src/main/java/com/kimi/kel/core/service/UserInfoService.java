package com.kimi.kel.core.service;

import com.kimi.kel.core.pojo.entities.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kimi.kel.core.pojo.vo.LoginVO;
import com.kimi.kel.core.pojo.vo.RegisterVO;
import com.kimi.kel.core.pojo.vo.UserInfoVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kimi
 * @since 2022-02-06
 */
public interface UserInfoService extends IService<UserInfo> {

    void register(RegisterVO registerVO);

    UserInfoVO login(LoginVO loginVO, String ip);
}
