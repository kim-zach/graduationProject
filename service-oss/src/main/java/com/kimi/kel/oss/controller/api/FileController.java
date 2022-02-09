package com.kimi.kel.oss.controller.api;

import com.kimi.common.exception.BusinessException;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Api(tags = "阿里云文件管理")
//@CrossOrigin
@RequestMapping("/api/oss/file")
public class FileController {

    @Resource
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file") MultipartFile file,

            @ApiParam(value = "模块",required = true)
            @RequestParam("module") String module
    ){
        String url = null;
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            url = fileService.upload(inputStream, module, fileName);
            return R.ok().message("文件上传成功").data("url",url);

        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }

    }


    @ApiOperation("删除OSS文件" )
    @DeleteMapping("/remove")
    public R remove(
            @ApiParam(value = "url",required = true)
            @RequestParam("url") String url
    ){
        if(fileService.removeFile(url)){
        return R.ok().message("删除成功");}
        else{
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("上传用户头像")
    @PostMapping("/uploadUserImage")
    public String uploadUserImage(
            @ApiParam(value = "文件", required = true)
            @RequestParam("file") MultipartFile file,

            @ApiParam(value = "模块",required = true)
            @RequestParam("module") String module
                             ){
        String url = null;
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            url = fileService.upload(inputStream, module, fileName);
            return url;

        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }
}

