package com.kimi.kel.vod.controller.api;

import com.kimi.common.result.R;
import com.kimi.kel.vod.service.impl.VodService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file){

        String videoId = vodService.uploadVideo(file);
        return R.ok().data("videoId",videoId);
    }
}
