package com.kimi.kel.core.client;

import com.kimi.common.result.R;
import com.kimi.kel.core.config.FeignMultipartSupportConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "service-vod",configuration = FeignMultipartSupportConfig.class)
public interface VodUploadVideoClient {

    @ApiOperation(value = "视频上传")
    @PostMapping(value = "/api/vod/uploadVideo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  //访问地址要写全
    String uploadVideo(
            @ApiParam(value = "文件", required = true)
            @RequestPart(value = "file") MultipartFile file);

    @ApiOperation("删除视频")
    @DeleteMapping("/api/vod/deleteVideo/{videoSourceId}")
    Boolean deleteVideo(
            @ApiParam(value = "视频云id", required = true)
            @RequestPart(value = "videoSourceId") String videoSourceId
    );

    @ApiOperation("获取视频播放凭证")
    @GetMapping ("/api/vod/getPlayAuthByVideoSourceId/{videoSourceId}")
    String getPlayAuthByVideoSourceId(
            @PathVariable(value = "videoSourceId") String videoSourceId);
}
