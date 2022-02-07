package com.kimi.kel.core.service.impl;

import com.kimi.kel.core.pojo.entities.UserLoginRecord;
import com.kimi.kel.core.mapper.UserLoginRecordMapper;
import com.kimi.kel.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author kimi
 * @since 2022-02-06
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

}
