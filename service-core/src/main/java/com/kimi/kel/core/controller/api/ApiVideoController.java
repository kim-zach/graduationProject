package com.kimi.kel.core.controller.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.core.client.VodUploadVideoClient;
import com.kimi.kel.core.pojo.entities.Video;
import com.kimi.kel.core.pojo.entities.WordVocabularyNotebook;
import com.kimi.kel.core.pojo.vo.VideoDetailsVO;
import com.kimi.kel.core.pojo.vo.VideoInfoVO;
import com.kimi.kel.core.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-02-18
 */
@Api("用户传视频")
@Slf4j
@RestController
@RequestMapping("/api/core/video")
public class ApiVideoController {

    @Resource
    private VodUploadVideoClient vodUploadVideoClient;


    @Resource
    private VideoService videoService;

    @ApiOperation("视频上传")
    @PostMapping("/uploadVideo")
    public R  uploadVideo(
            @ApiParam(value = "视频文件", required = true)
            @RequestParam("file") MultipartFile file

    ){
        String videoSourceId = vodUploadVideoClient.uploadVideo(file);

        if(videoSourceId != null){
            return R.ok().data("videoSourceId",videoSourceId);
        }else{
            return R.error().message("上传视频失败");
        }

    }


    @ApiOperation("提交视频表单")
    @PostMapping("/submitVideoForm")
    public R submitVideoForm(
            @ApiParam(value = "视频信息",required = true)
            @RequestBody VideoInfoVO videoInfoVO
    ){
        //校验userId
        Assert.notNull(videoInfoVO.getUserId(), ResponseEnum.USER_ID_NOT_NULL_ERROR);
        //校验VideoInfoVO
        Assert.notNull(videoInfoVO.getTag(),ResponseEnum.TAG_NOT_NULL_ERROR);
        Assert.notNull(videoInfoVO.getTitle(),ResponseEnum.TITLE_NOT_NULL_ERROR);
        Assert.notNull(videoInfoVO.getBrief(),ResponseEnum.BRIEF_NOT_NULL_ERROR);
        Assert.notNull(videoInfoVO.getVideoSourceId(),ResponseEnum.VIDEO_SOURCE_ID_NOT_NULL_ERROR);

        boolean result = false;


        result  = videoService.saveVideo(videoInfoVO);

        if(result){
            return R.ok().message("提交成功");
        }else{
            return R.error().message("提交失败");
        }

    }


    @ApiOperation("获取视频列表")
    @GetMapping("/getVideoListByUserId/{page}/{limit}/{userId}")
    public R getVideoListByUserId(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page ,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "用户id",required = true)
            @PathVariable String userId
    ){

        Page<Video> videoPage = new Page<>(page,limit);
        IPage<Video> pageModel = videoService.getVideoListByUserId(videoPage,userId);

        log.info("pageModel:{}",pageModel);
        return R.ok().data("pageModel",pageModel);
    }


    @ApiOperation("删除上传视频")
    @DeleteMapping("/deleteVideoById/{videoId}/{videoSourceId}")
    public R deleteVideoById(
            @ApiParam(value = "视频id",required = true)
            @PathVariable Long videoId,
            @ApiParam(value = "视频云id",required = true)
            @PathVariable String videoSourceId
    ){
        //删除表中视频，同时删除云中的视频
        boolean result = videoService.deleteVideo(videoId,videoSourceId);
        if(result){
            return R.ok().message("视频下架成功");
        }else{
            return R.error().message("视频下架出错");
        }
    }

    @ApiOperation("获取视频播放凭证")
    @GetMapping("/getPlayAuthByVideoSourceId/{videoSourceId}")
    public R getPlayAuthByVideoSourceId(
            @ApiParam(value = "视频云id",required = true)
            @PathVariable String videoSourceId
    ){
        String playAuth = vodUploadVideoClient.getPlayAuthByVideoSourceId(videoSourceId);
        if(StringUtils.isNotBlank(playAuth)){
            return R.ok().data("playAuth",playAuth);
        }
        return R.error().message("获取视频播放凭证失败");
    }

    @ApiOperation(("获取视频详细信息"))
    @GetMapping("/getVideoDetailsById/{videoId}")
    public R getVideoDetailsById(
            @ApiParam(value = "视频id",required = true)
            @PathVariable Long videoId
    ){
        Assert.notNull(videoId,ResponseEnum.VIDEO_ID_NOT_NULL_ERROR);
        VideoDetailsVO videoDetailsVO = videoService.getVideoDetailsVOById(videoId);
        if(videoDetailsVO != null){
            return R.ok().data("videoDetails",videoDetailsVO);
        }
        return R.error().message("获取视频信息失败");
    }

    @ApiOperation("获取通过审核的视频列表")
    @GetMapping("/getVideoListApproved/{page}/{limit}/{status}")
    public R getVideoListApproved(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page ,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "审核状态",required = true)
            @PathVariable Integer status,
            @ApiParam(value = "查询条件",required = true)
            String searchObj
    ){
        Page<Video> videoPage = new Page<>(page,limit);
        IPage<Video> pageModel = videoService.getVideoListApproved(videoPage,status,searchObj);

        log.info("pageModel:{}",pageModel);
        return R.ok().data("pageModel",pageModel);
    }

    @ApiOperation("增加视频播放量")
    @GetMapping("/addViewAmount/{videoId}")
    public R addViewAmount(
            @ApiParam(value = "视频ID",required = true)
            @PathVariable Long videoId
    ){
        Assert.notNull(videoId,ResponseEnum.VIDEO_ID_NOT_NULL_ERROR);
        boolean result =  videoService.addViewAmount(videoId);
        if(result){
            return R.ok();
        }
        return R.error();
    }


    @ApiOperation("点赞视频")
    @GetMapping("/likeOrDisLikeVideo/{videoId}/{favor}")
    public R likeorDisLikeVideo(
            @ApiParam(value = "视频ID",required = true)
            @PathVariable Long videoId,
            @ApiParam(value = "0点赞，1取消",required = true)
            @PathVariable Integer favor
    ){
        Assert.notNull(videoId,ResponseEnum.VIDEO_ID_NOT_NULL_ERROR);
        Assert.notNull(favor,ResponseEnum.VIDEO_ID_NOT_NULL_ERROR);
        boolean result =  videoService.likeOrDisLikeVideo(videoId,favor);
        if(result){
            return R.ok();
        }
        return R.error();
    }
}

