package com.program.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OssService {

    //上传头像到oss
    String uploadFileAvatar(MultipartFile file);

    void updateSalt(MultipartFile salt,String id);

}
