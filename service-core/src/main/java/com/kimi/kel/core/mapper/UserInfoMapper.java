package com.kimi.kel.core.mapper;

import com.kimi.kel.core.pojo.entities.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kimi
 * @since 2022-02-06
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    String getAvatar() ;

    String getUserName(Long userId) ;


}
