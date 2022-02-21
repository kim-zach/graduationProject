package com.kimi.kel.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.common.result.R;
import com.kimi.kel.core.pojo.entities.Video;
import com.kimi.kel.core.pojo.query.VideoInfoQuery;
import com.kimi.kel.core.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-02-18
 */
@Slf4j
@Api("管理视频列表")
@RestController
@RequestMapping("/admin/core/video")
public class AdminVideoController {

    @Resource
    private VideoService videoService;


    @ApiOperation("获取视频列表")
    @GetMapping("/list/{page}/{limit}")
    public R getVideoPageList(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page ,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "查询对象",required = true)
                    VideoInfoQuery videoInfoQuery
    ){

        Page<Video> videoPage = new Page<>(page,limit);
        IPage<Video> pageModel = videoService.getVideoPageList(videoPage,videoInfoQuery);

        log.info("pageModel:{}",pageModel);
        return R.ok().data("pageModel",pageModel);
    }
}

