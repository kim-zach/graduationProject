package com.kimi.kel.oss.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface FileService {

    /**
     * 文件上传到阿里云
     */
    String upload(InputStream inputStream,String module,String fileName);

    boolean removeFile(String url);
}

