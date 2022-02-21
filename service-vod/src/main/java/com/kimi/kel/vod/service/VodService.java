package com.kimi.kel.vod.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface VodService {
    String uploadVideo(MultipartFile file);

    boolean deleteVideo(String videoSourceId);

    String getPlayAuthByVideoSourceId(String videoSourceId);
}
