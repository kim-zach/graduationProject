package com.kimi.kel.vod.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface VodService {
    String uploadVideo(MultipartFile file);

}
