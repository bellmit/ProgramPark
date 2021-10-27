package com.program.user.client;

import com.program.atc.entity.vo.ArticleVo;
import com.program.commonutils.R;
import com.program.user.config.FeignConfig1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(name="program-article")
public interface ArticleClient {
    @GetMapping("/ProgramPark/article/getArticleCount")
    public String getArticleCount(@RequestParam String id);

    @GetMapping("/ProgramPark/article/getHandCount")
    public String getHandCount(@RequestParam String id);

    @GetMapping("/ProgramPark/article/comment/getCommentCount")
    public String getOneComment(@RequestParam String id);

    @PostMapping("/ProgramPark/article/updateArticleInfo")
    public R updateCourseInfo(@RequestBody ArticleVo articleVo);

    @GetMapping("/ProgramPark/article/getArticles/{id}/{name}")
    public void getAtcIds(@PathVariable("id") String id,@PathVariable("name") String name);
}
