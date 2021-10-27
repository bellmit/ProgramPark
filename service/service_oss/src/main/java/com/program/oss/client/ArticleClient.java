package com.program.oss.client;


import com.program.commonutils.vo.UrlAndId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(name="program-article")
public interface ArticleClient {
    @PostMapping("/ProgramPark/article/getArticles1")
    public void getAtcIds1(@RequestBody UrlAndId urlAndId);
}
