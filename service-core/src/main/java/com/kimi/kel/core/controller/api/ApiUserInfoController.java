package com.kimi.kel.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.common.util.RegexValidateUtils;
import com.kimi.kel.base.util.JwtUtils;
import com.kimi.kel.core.client.OssUploadAvatarClient;
import com.kimi.kel.core.pojo.vo.LoginVO;
import com.kimi.kel.core.pojo.vo.RegisterVO;
import com.kimi.kel.core.pojo.vo.UserInfoDetailsVO;
import com.kimi.kel.core.pojo.vo.UserInfoVO;
import com.kimi.kel.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javafx.beans.property.ReadOnlyObjectProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.PublicKey;

import static com.kimi.kel.core.pojo.entities.Constant.AVATAR;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-02-06
 */
@Api(tags = "用户接口")
//@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/core/userInfo")
public class ApiUserInfoController {


    public static final long LIMIT_SIZE_OF_THE_AVATAR = 10485760l;
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private OssUploadAvatarClient ossUploadAvatarClient;

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO){

        //声明变量方便校验，防止恶意攻击
        String code = registerVO.getCode();
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();

        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(code,ResponseEnum.CODE_NULL_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        //校验验证码是否正确
        String codeGen = (String)redisTemplate.opsForValue().get("kel:sms:code:" + registerVO.getMobile());
        Assert.equals(code,codeGen, ResponseEnum.CODE_ERROR);

        //注册
        userInfoService.register(registerVO);

        return R.ok().message("注册成功");
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO,HttpServletRequest request){

        String password = loginVO.getPassword();
        String mobile = loginVO.getMobile();

        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);

        //远程主机 客户端地址
        String ip = request.getRemoteAddr();
        UserInfoVO userInfoVO = userInfoService.login(loginVO, ip);

        return R.ok().data("userInfo",userInfoVO);
    }

    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request){

        String token = request.getHeader("token");
        boolean result = JwtUtils.checkToken(token);

        if(result){
            return R.ok();
        }else{
            return R.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }

    @ApiOperation("校验手机号是否注册")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile){
        return userInfoService.checkMobile(mobile);
    }

    @ApiOperation("取出用户个人信息")
    @GetMapping("/getUserDetailsByMobile/{mobile}")
    public R getUserDetailsByMobile(@PathVariable String mobile){
        UserInfoDetailsVO userInfoDetails = userInfoService.getUserDetailsByMobile(mobile);
        return R.ok().data("userInfo",userInfoDetails);
    }

    @ApiOperation("修改用户个人信息")
    @PostMapping("/editUserDetails")
    public R editUserDetails(@RequestBody UserInfoDetailsVO userInfoDetailsVO){
        boolean result = userInfoService.editUserDetails(userInfoDetailsVO);
        if(result  ){
        return  R.ok().message("修改成功");}else{
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("判断用户昵称是否存在")
    @GetMapping("/validUserNickName/{nickName}")
    public R validUserNickName(@PathVariable String nickName){
        boolean result = userInfoService.validUserNickName(nickName);
        if(!result){
            return R.ok().message("用户名不存在，可注册");
        }else{
            return R.error().message("用户名已存在");
        }
    }


    @ApiOperation("修改昵称")
    @GetMapping("/editNickName/{nickName}/{id}")
    public R editNickName(@PathVariable String nickName,
                          @PathVariable Long id){
        boolean result = userInfoService.editNickName(nickName,id);
        if(result){
            return R.ok().message("修改成功");
        }else{
            return R.error().message("用户名已存在");
        }
    }

    @ApiOperation("用户上传头像")
    @PostMapping("/uploadAvatarImage/{id}")
    public R uploadAvatarImage(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file") MultipartFile file,

            @ApiParam(value = "id",required = true)
            @PathVariable("id") Long id
    )
    {
        String module = AVATAR;

        long size = file.getSize();
        if(size > LIMIT_SIZE_OF_THE_AVATAR){
            return R.error().message("文件过大");
        }

        String avatarUrl = ossUploadAvatarClient.uploadUserImage(file, module);
        avatarUrl = (String)JSON.parse(avatarUrl);
        boolean result = userInfoService.updateUserAvatar(id,avatarUrl);

        if(result){
            return R.ok().message("更新头像成功");
        }else{
            return R.error().message("更新头像失败");
        }
    }
}

