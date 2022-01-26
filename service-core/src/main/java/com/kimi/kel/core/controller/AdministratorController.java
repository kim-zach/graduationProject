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
@CrossOrigin
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
        return R.ok().data("roles", "[admin]").data("name", "admin").data("avatar", "https://image.baidu.com/search/detail?tn=baiduimagedetail&word=%E5%AE%A0%E7%89%A9%E5%9B%BE%E7%89%87&album_tab=%E5%8A%A8%E7%89%A9&album_id=688&ie=utf-8&fr=albumsdetail&cs=2405382010,1555992666&pi=227262&pn=3&ic=0&objurl=https%3A%2F%2Ft7.baidu.com%2Fit%2Fu%3D2405382010%2C1555992666%26fm%3D193%26f%3DGIF");
    }

    @PostMapping("user/logout")
    public R logOut(){
        return R.ok().data("token","admin");
    }

}

