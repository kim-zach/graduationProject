package com.kimi.kel.core.client;

import com.kimi.kel.core.config.FeignMultipartSupportConfig;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "service-oss",configuration = FeignMultipartSupportConfig.class)
public interface OssUploadAvatarClient {

    @ApiOperation(value = "用户头像上传")
    @PostMapping(value = "/api/oss/file/uploadUserImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  //访问地址要写全
    String uploadUserImage(
            @ApiParam(value = "文件", required = true)
            @RequestPart(value = "file") MultipartFile file,

            @ApiParam(value = "模块",required = true)
            @RequestParam("module") String module);
}
