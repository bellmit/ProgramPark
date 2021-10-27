package com.program.user.client;

import com.program.atc.config.FeignSupportConfig;
import com.program.commonutils.vo.UrlsVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Component
@FeignClient(name="program-oss",configuration = FeignSupportConfig.class)
public interface OssClient {

    @PostMapping("/ProgramPark/oss/getUrlsById/{id}")
    public UrlsVo getUrlsById(@PathVariable("id") String id);
}
