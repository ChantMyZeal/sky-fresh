package com.sky.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 返回文件的请求路径
     */
    String upload(MultipartFile file);

}
