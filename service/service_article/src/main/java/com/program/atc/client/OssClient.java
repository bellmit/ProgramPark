package com.program.atc.client;

import com.program.atc.config.FeignConfig;
import com.program.atc.config.FeignSupportConfig;
import com.program.commonutils.R;
import com.program.commonutils.vo.UrlsVo;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@FeignClient(name="program-oss",configuration = FeignSupportConfig.class)
public interface OssClient {
    @PostMapping(value = "/ProgramPark/oss/{aid}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadOssFile(@PathVariable("aid") String aid
            , @RequestPart MultipartFile files);


    @PostMapping("/ProgramPark/oss/getUrlsById/{id}")
    public UrlsVo getUrlsById(@PathVariable("id") String id);
}
