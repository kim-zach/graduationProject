package com.kimi.kel.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.kimi.kel.vod.service.VodService;
import com.kimi.kel.vod.util.InitialVodClient;
import com.kimi.kel.vod.util.VodProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class VodServiceImpl implements VodService {

    @Override
    public String uploadVideo(MultipartFile file) {
        //fileName ：上传文件原始名称
        String fileName = file.getOriginalFilename();
        //title：上传之后显示名称
        //不要视频格式后缀
        String title = fileName.substring(0,fileName.lastIndexOf("."));
        //inputStream 上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();

        String videoId = null;

        UploadStreamRequest request = new UploadStreamRequest(VodProperties.KEY_ID, VodProperties.KEY_SECRET, title, fileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            videoId = response.getVideoId();
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            videoId = response.getVideoId();
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
        return videoId;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteVideo(String videoSourceId) {

        try {
            //创建处理client
            DefaultAcsClient client = InitialVodClient.initVodClient(VodProperties.KEY_ID, VodProperties.KEY_SECRET);
            //创建request
            DeleteVideoRequest request = new DeleteVideoRequest();
            //request对象中set要删除的videoId
            request.setVideoIds(videoSourceId.substring(1,videoSourceId.length()-1));
            //调用client 删除视频
             client.getAcsResponse(request);
            return true;

        } catch (ClientException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String getPlayAuthByVideoSourceId(String videoSourceId) {

        //创建Client
        DefaultAcsClient client = null;
        try {
            client = InitialVodClient.initVodClient(VodProperties.KEY_ID,VodProperties.KEY_SECRET );

        //创建request response 对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        //set 视频id
        request.setVideoId(videoSourceId.substring(1,videoSourceId.length()-1));

        response = client.getAcsResponse(request);
        String playAuth = response.getPlayAuth();
        System.out.println("playAuth:" +response.getPlayAuth());
        return playAuth;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }

    }
}
