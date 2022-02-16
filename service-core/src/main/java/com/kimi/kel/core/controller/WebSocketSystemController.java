package com.kimi.kel.core.controller;

import com.kimi.common.result.R;
import com.kimi.kel.core.service.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController("web_Scoket_system")
@RequestMapping("/api/core/socket")
public class WebSocketSystemController {

    public static final double TIME_OVER = 3;

    @Resource
    private WebSocketServer webSocketServer;

    //页面请求
    @ApiOperation("test")
    @GetMapping("/index/{userId}")
    public R socket(@PathVariable String userId,HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/socket1");
//        HttpSession session = request.getSession();
//        webSocketServer.generateWordGatherInfo(userId);
        mav.addObject("userId", userId);
//        mav.addObject("session",session);
        try {
            webSocketServer.sendInfo("test20220216","13060821982");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok().data("mav",mav);
    }

    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public Map pushToWeb(@PathVariable String cid, String message) {
        Map<String,Object> result = new HashMap<>();
        try {
//            WebSocketServer.sendInfo(message, cid);
            WebSocketServer.sendInfo(message, null);
            result.put("code", cid);
            result.put("msg", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    @ApiOperation("服务端接收请求数据")
    @GetMapping("/judgeWhetherAWord/{word}")
    public R judgeWhetherAWord(
            @ApiParam(value = "单词",required = true)
            @PathVariable String word
    ){
        //判断是否单词
        //是则返回消息给客户端
        try {
            webSocketServer.sendInfo("test20220216","13060821982");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok().message("");
    }



}