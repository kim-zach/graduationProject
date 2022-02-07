package com.kimi.kel.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.common.util.RegexValidateUtils;
import com.kimi.kel.base.util.JwtUtils;
import com.kimi.kel.core.pojo.entities.UserInfo;
import com.kimi.kel.core.pojo.query.UserInfoQuery;
import com.kimi.kel.core.pojo.vo.LoginVO;
import com.kimi.kel.core.pojo.vo.RegisterVO;
import com.kimi.kel.core.pojo.vo.UserInfoVO;
import com.kimi.kel.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-02-06
 */
@Api(tags = "用户管理")
//@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/admin/core/userInfo")
public class AdminUserInfoController {



    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("获取用户分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R listPage(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page ,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "查询对象", required = false)
                    UserInfoQuery userInfoQuery){

        Page<UserInfo> userInfoPage = new Page<>(page,limit);
        IPage<UserInfo> pageModel = userInfoService.listPage(userInfoPage,userInfoQuery);

        return R.ok().data("pageModel",pageModel);
    }


    @ApiOperation("锁定和解锁")
    @PutMapping("/lock/{id}/{status}")
    public R lock(
            @ApiParam(value = "用户id", required = true)
            @PathVariable("id") Long id,

            @ApiParam(value = "用户status", required = true)
            @PathVariable("status") Integer status) {

        userInfoService.lock(id,status);

        return R.ok().message(status == 1?"解锁成功":"锁定成功");
    }



}

