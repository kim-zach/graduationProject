package com.kimi.kel.vod.controller.api;

import com.kimi.common.result.R;
import com.kimi.kel.vod.service.VodService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/vod")
@Slf4j
@Api("视频上传")
public class ApiVodController {

    @Resource
    private VodService vodService;

    //上传视频到阿里云
    @PostMapping("/uploadVideo")
    public String uploadVideo(MultipartFile file){

        String videoId = vodService.uploadVideo(file);
        return videoId;
    }

    //删除阿里云中的视频
    @DeleteMapping("/deleteVideo/{videoSourceId}")
    public Boolean deleteVideo(
            @PathVariable("videoSourceId") String videoSourceId
    ){
        boolean result =  vodService.deleteVideo(videoSourceId);
        if(result){
            return true;
        }
        return false;
    }

    //获取视频播放凭证
    @GetMapping("/getPlayAuthByVideoSourceId/{videoSourceId}")
    public String getPlayAuthByVideoSourceId(
            @PathVariable("videoSourceId") String videoSourceId){
        String playAuth = vodService.getPlayAuthByVideoSourceId(videoSourceId);
        if(StringUtils.isNotBlank(playAuth)){
            return playAuth;
        }
        return null;
    }
}
