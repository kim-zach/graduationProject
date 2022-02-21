package com.kimi.kel.core.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.core.pojo.entities.Video;
import com.kimi.kel.core.pojo.entities.VideoCollect;
import com.kimi.kel.core.pojo.entities.VideoLike;
import com.kimi.kel.core.service.VideoCollectService;
import com.kimi.kel.core.service.VideoLikeService;
import com.kimi.kel.core.service.VideoService;
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
@RequestMapping("/api/core/videoLike")
public class ApiVideoLikeController {

    @Resource
    private VideoLikeService videoLikeService;

    @Resource
    private VideoCollectService videoCollectService;

    @Resource
    private VideoService videoService;

    @ApiOperation("用户是否已经点赞")
    @GetMapping("/fetchLikeStatus/{userId}/{videoId}")
    public R fetchLikeStatus(
            @ApiParam(value = "用户id",required = true)
            @PathVariable Long userId,
            @ApiParam(value = "视频id",required = true)
            @PathVariable Long videoId
    ){
        Assert.notNull(videoId, ResponseEnum.VIDEO_ID_NOT_NULL_ERROR);
        Assert.notNull(userId, ResponseEnum.USER_ID_NOT_NULL_ERROR);

        QueryWrapper<VideoLike> videoLikeQueryWrapper = new QueryWrapper<>();
        videoLikeQueryWrapper
                .eq("user_id",userId)
                .eq("video_id",videoId);
        VideoLike one = videoLikeService.getOne(videoLikeQueryWrapper);
        if(one != null){
            return R.ok().message("alreadyLiked");
        }
        return R.ok().message("hasNotLikedYet") ;//R.error().message("hasNotLikedYet")
    }


    @ApiOperation("用户是否已经收藏")
    @GetMapping("/fetchCollectStatus/{userId}/{videoId}")
    public R fetchCollectStatus(
            @ApiParam(value = "用户id",required = true)
            @PathVariable Long userId,
            @ApiParam(value = "视频id",required = true)
            @PathVariable Long videoId
    ){
        Assert.notNull(userId, ResponseEnum.USER_ID_NOT_NULL_ERROR);
        Assert.notNull(videoId, ResponseEnum.VIDEO_ID_NOT_NULL_ERROR);

        QueryWrapper<VideoCollect> videoCollectQueryWrapper = new QueryWrapper<>();
        videoCollectQueryWrapper
                .eq("user_id",userId)
                .eq("video_id",videoId);
        VideoCollect one = videoCollectService.getOne(videoCollectQueryWrapper);
        if(one != null){
            return R.ok().message("alreadyCollected");
        }
        return R.ok().message("hasNotCollectYet") ;//R.error().message("hasNotLikedYet")
    }

    @ApiOperation("获取用户喜欢的视频列表")
    @GetMapping("/getvideoLikedListByUserId/{page}/{limit}/{userId}")
    public R getvideoLikedListByUserId(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page ,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "用户id",required = true)
            @PathVariable Long userId
    ){
        Assert.notNull(userId, ResponseEnum.VIDEO_ID_NOT_NULL_ERROR);
        //先从video_like表中查出收藏的video list,再查video的详细信息
        List<Long> videoIdList =  videoLikeService.getVideoIdListByUserId(userId);

        Page<Video> videoPage = new Page<>(page,limit);
        IPage<Video> pageModel = videoService.getVideoDetailsByVideoIdList(videoPage,videoIdList);

        log.info("pageModel:{}",pageModel);
        return R.ok().data("pageModel",pageModel);
    }
}

