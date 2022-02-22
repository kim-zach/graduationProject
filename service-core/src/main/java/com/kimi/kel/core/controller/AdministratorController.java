package com.kimi.kel.core.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.common.util.MD5;
import com.kimi.kel.core.pojo.entities.Administrator;
import com.kimi.kel.core.pojo.vo.UserLoginVO;
import com.kimi.kel.core.service.AdministratorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-01-23
 */
//@CrossOrigin
@RestController
@RequestMapping("/admin/core")
public class AdministratorController {

    public static final int LIMIT_PASSWORD_LENGTH = 6;
    public static final int SUPER_ADMINISTRATOR = 1;
    @Resource
    AdministratorService administratorService;


    @PostMapping("/user/login")
//    public R login(@RequestParam("username") String userName,
//                   @RequestParam("password") String password) {
    public R login(@RequestBody UserLoginVO userLoginVO) {
        boolean result = administratorService.ifExists(userLoginVO.getUserName(), userLoginVO.getPassword());

        if (result) {
            //token is right now hardCode , needed to update
            return R.ok().message("登录成功").data("token", "admin");
        } else {
            return R.error().message("登录失败，查询不到用户");
        }
    }

    @GetMapping("/user/info")
    public R getInfo() {
        String url = new String("https://kel-file-kimi.oss-cn-shenzhen.aliyuncs.com/avatar/-2505434.gif");
        return R.ok().data("roles", "[admin]").data("name", "admin").data("avatar", "url");
    }

    @PostMapping("/user/logout")
    public R logOut(){
        return R.ok().data("token","admin");
    }


    @PostMapping("/user/addAdministrator")
    public R addAdministrator(
           @RequestBody  UserLoginVO admin
    ){
        String userName = admin.getUserName();
        String password = admin.getPassword();

        Assert.notNull(userName, ResponseEnum.USERNAME_NOT_NULL_ERROR);
        Assert.notNull(password, ResponseEnum.PASSWORD_NOT_NULL_ERROR);

        if(password.length() < LIMIT_PASSWORD_LENGTH){
            return R.setResult(ResponseEnum.PASSWORD_LENGTH_SHOULD_BE_LONGER_THAN_SIX);
        }

        password = MD5.encrypt(password);

        //管理员存在
        QueryWrapper<Administrator> administratorQueryWrapper = new QueryWrapper<>();
        administratorQueryWrapper.eq("user_name",userName);
        Administrator one = administratorService.getOne(administratorQueryWrapper);
        if(one != null){
            return R.error().message("管理员已经存在");
        }

        Administrator administrator = new Administrator();
        administrator.setUserName(userName);
        administrator.setPassword(password);

        boolean save = administratorService.save(administrator);

        if(save){
            return R.ok().message("添加成功");
        }
        return R.error().message("添加失败");

    }
}

