package com.kimi.kel.core.controller.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.core.pojo.entities.Video;
import com.kimi.kel.core.service.VideoCollectService;
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
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-02-21
 */
@Slf4j
@RestController
@RequestMapping("/api/core/videoCollect")
public class ApiVideoCollectController {

    @Resource
    private VideoService videoService;

    @Resource
    private VideoCollectService videoCollectService;

    @ApiOperation("获取收藏的视频列表")
    @GetMapping("/getvideoCollectListByUserId/{page}/{limit}/{userId}")
    public R getvideoCollectListByUserId(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page ,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "用户id",required = true)
            @PathVariable Long userId
    ){
        Assert.notNull(userId, ResponseEnum.VIDEO_ID_NOT_NULL_ERROR);
        //先从video_collect表中查出收藏的video list,再查video的详细信息
        List<Long> videoIdList = videoCollectService.getVideoIdListByUserId(userId);

        Page<Video> videoPage = new Page<>(page,limit);
        IPage<Video> pageModel = videoService.getVideoDetailsByVideoIdList(videoPage,videoIdList);

        log.info("pageModel:{}",pageModel);
        return R.ok().data("pageModel",pageModel);

    }
}

