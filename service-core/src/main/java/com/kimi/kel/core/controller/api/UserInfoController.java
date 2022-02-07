package com.kimi.kel.core.controller.api;


import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.common.util.RegexValidateUtils;
import com.kimi.kel.base.util.JwtUtils;
import com.kimi.kel.core.pojo.vo.LoginVO;
import com.kimi.kel.core.pojo.vo.RegisterVO;
import com.kimi.kel.core.pojo.vo.UserInfoVO;
import com.kimi.kel.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-02-06
 */
@Api(tags = "用户接口")
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/core/userInfo")
public class UserInfoController {

    @Resource
    private RedisTemplate redisTemplate;

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
}

