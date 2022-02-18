package com.kimi.kel.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.ResponseEnum;
import com.kimi.common.util.MD5;
import com.kimi.common.util.RegexValidateUtils;
import com.kimi.kel.base.util.JwtUtils;
import com.kimi.kel.core.mapper.UserLoginRecordMapper;
import com.kimi.kel.core.pojo.entities.Constant;
import com.kimi.kel.core.pojo.entities.UserInfo;
import com.kimi.kel.core.mapper.UserInfoMapper;
import com.kimi.kel.core.pojo.entities.UserLoginRecord;
import com.kimi.kel.core.pojo.query.UserInfoQuery;
import com.kimi.kel.core.pojo.vo.LoginVO;
import com.kimi.kel.core.pojo.vo.RegisterVO;
import com.kimi.kel.core.pojo.vo.UserInfoDetailsVO;
import com.kimi.kel.core.pojo.vo.UserInfoVO;
import com.kimi.kel.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kimi.kel.core.service.UserLoginRecordService;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kimi
 * @since 2022-02-06
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserLoginRecordService userLoginRecordService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public void register(RegisterVO registerVO) {

        //判断用户是否被注册
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",registerVO.getMobile());
        Integer result = baseMapper.selectCount(userInfoQueryWrapper);
        Assert.isTrue(result == 0, ResponseEnum.MOBILE_EXIST_ERROR);


        //插入用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile(registerVO.getMobile());
        userInfo.setName(registerVO.getMobile());
        userInfo.setNickName(registerVO.getMobile());
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));
        userInfo.setStatus(Constant.STATUS_NORMAL);
        userInfo.setAvatar(Constant.USER_AVATAR);

        baseMapper.insert(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoVO login(LoginVO loginVO, String ip) {

        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        //判断用户是否存在
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",mobile);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        Assert.notNull(userInfo ,ResponseEnum.LOGIN_MOBILE_ERROR);

        //密码是否正确
        Assert.equals(MD5.encrypt(password),userInfo.getPassword(),ResponseEnum.LOGIN_PASSWORD_ERROR);

        //用户是否被禁用
        Assert.equals(userInfo.getStatus(), Constant.STATUS_NORMAL,ResponseEnum.LOGIN_LOKED_ERROR);

        //记录登录日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);

        userLoginRecordService.save(userLoginRecord);

        //生成token
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());

        //组装userInfoVO
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(userInfo.getId());
        userInfoVO.setToken(token);
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        userInfoVO.setAvatar(userInfo.getAvatar());
        userInfoVO.setMobile(userInfo.getMobile());


        return userInfoVO;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> userInfoPage, UserInfoQuery userInfoQuery) {
        //没有查询条件，展示所有用户
        if(userInfoQuery == null){
           return  baseMapper.selectPage(userInfoPage,null);
        }

        //有查询条件，封装查询条件
        String mobile = userInfoQuery.getMobile();
        Integer status = userInfoQuery.getStatus();
        String name = userInfoQuery.getName();
        String nickName = userInfoQuery.getNickName();

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.like(StringUtils.isNotBlank(mobile),"mobile",mobile)
        .like(status != null,"status",status)
        .like(name != null,"name",name)
        .like(nickName != null,"nick_name",nickName);

        return  baseMapper.selectPage(userInfoPage,userInfoQueryWrapper);

    }

    @Override
    public void lock(Long id, Integer status) {

        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    @Override
    public boolean checkMobile(String mobile) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",mobile);
        Integer result = baseMapper.selectCount(userInfoQueryWrapper);

        return result > 0;
    }

    @Override
    public UserInfoDetailsVO getUserDetailsByMobile(String mobile) {

        //号码是否为空
        Assert.notNull(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        //号码是否正确
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",mobile);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);

        UserInfoDetailsVO userInfoDetailsVO = new UserInfoDetailsVO();

        userInfoDetailsVO.setId(userInfo.getId());
        userInfoDetailsVO.setMobile(userInfo.getMobile());
        userInfoDetailsVO.setName(userInfo.getName());
        userInfoDetailsVO.setAddress(userInfo.getAddress());
        userInfoDetailsVO.setEmail(userInfo.getEmail());
        userInfoDetailsVO.setAvatar(userInfo.getAvatar());
        userInfoDetailsVO.setNickName(userInfo.getNickName());


        return userInfoDetailsVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean editUserDetails(UserInfoDetailsVO userInfoDetailsVO) {

        UserInfo userInfo = new UserInfo();

        userInfo.setId(userInfoDetailsVO.getId());

        //TODO
//        userInfo.setAvatar(userInfoDetailsVO.getAvatar());

        userInfo.setNickName(userInfoDetailsVO.getNickName());
        userInfo.setName(userInfoDetailsVO.getName());
        userInfo.setEmail(userInfoDetailsVO.getEmail());
        userInfo.setAddress(userInfoDetailsVO.getAddress());


        int result = baseMapper.updateById(userInfo);
        return result > 0;
    }

    @Override
    public boolean validUserNickName(String nickName) {

        Assert.notNull(nickName,ResponseEnum.NICK_NAME_NULL_ERROR);

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("nick_name",nickName);
        Integer result = baseMapper.selectCount(userInfoQueryWrapper);

        if(result > 0){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean editNickName(String nickName,Long id) {

        Assert.notNull(nickName,ResponseEnum.NICK_NAME_NULL_ERROR);

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("nick_name",nickName);
        Integer result = baseMapper.selectCount(userInfoQueryWrapper);

        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setNickName(nickName);

        if(result > 0){
            return false;
        }else{
            return baseMapper.updateById(userInfo) > 0 ? true : false;
        }
    }

    @Override
    public boolean updateUserAvatar(Long id, String avatarUrl) {

        Assert.notNull(avatarUrl,ResponseEnum.AVATAR_URL_NULL_ERROR);

        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setAvatar(avatarUrl);

        int result = baseMapper.updateById(userInfo);

        return result > 0 ;
    }


}
