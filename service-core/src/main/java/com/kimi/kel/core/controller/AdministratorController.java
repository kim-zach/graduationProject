package com.kimi.kel.core.controller;


import com.kimi.common.result.R;
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

    @PostMapping("user/logout")
    public R logOut(){
        return R.ok().data("token","admin");
    }

}

