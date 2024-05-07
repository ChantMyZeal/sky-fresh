package com.sky.service.impl;

import com.sky.service.FileService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 返回文件的请求路径
     */
    @Override
    public String upload(MultipartFile file) {
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            assert originalFilename != null : "originalFilename == null";//断言原文件名不为空，否则在日志中输出错误信息
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //利用UUID构造新的不重复文件名
            String objectName = UUID.randomUUID() + extension;
            //返回文件的请求路径
            return aliOssUtil.upload(file.getBytes(), objectName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            log.info("文件上传失败");
        }
        return null;
    }

}
