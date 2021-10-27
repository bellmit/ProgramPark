package com.program.atc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.client.OssClient;
import com.program.atc.entity.Article;
import com.program.atc.entity.Comment;
import com.program.atc.entity.vo.ArticleFrontVo;
import com.program.atc.entity.vo.ArticleVo;
import com.program.atc.entity.vo.ArticleWarnVo;
import com.program.atc.service.ArticleHistoryService;
import com.program.atc.service.ArticleService;
import com.program.atc.service.CommentService;
import com.program.commonutils.PageUtil;
import com.program.commonutils.R;
import com.program.commonutils.vo.UrlAndId;
import com.program.security.security.TokenManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-09
 */
@Api(description="文章管理")
@RestController
@RequestMapping("/ProgramPark/article")
//@CrossOrigin //跨域
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleHistoryService articleHistoryService;

    @Autowired
    private OssClient ossClient;

    @Autowired
    private TokenManager tokenManager;

    //根据课程id查询文章信息
    @ApiOperation(value = "根据频道查询文章")
    @GetMapping("getAtcByCId/{channelId}/{page}")
    public R getCourseInfoDto(@ApiParam(name = "channelId", value = "频道ID", required = false)
                                  @PathVariable(value = "channelId",required = false) String channelId,
                              @ApiParam(name = "page", value = "文章的第几页", required = true)
                              @PathVariable long page) {
        Page<Article> pageCourse1 = new Page<>(page,PageUtil.size);
        Map<String,Object> map;
        if(channelId==null||"".equals(channelId)){
            map =articleService.getArticleInfo1(pageCourse1);
        }
        else{
            map =articleService.getArticleInfo(pageCourse1,channelId);
        }
        return R.ok().data("map",map);

    }
    @ApiOperation(value = "根据频道查询文章")
    @GetMapping("getAtcByCId/{page}")
    public R getCourseInfoDto(@ApiParam(name = "page", value = "文章的第几页", required = true)
                              @PathVariable long page) {
        Page<Article> pageCourse1 = new Page<>(page,PageUtil.size);
        Map<String,Object> map;
        map =articleService.getArticleInfo1(pageCourse1);
        return R.ok().data("map",map);

    }
    @ApiOperation(value = "根据文章ID查询文章")
    @GetMapping("getArticleById/{id}")
    public R getAtcById(@ApiParam(name = "id", value = "文章ID", required = true)
                                  @PathVariable String id,HttpServletRequest request) {
//        String memberId = "1";
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        ArticleVo articleVo =articleService.getArticleVo(id,memberId);
        articleHistoryService.saveArticleHistoryInfo(id,memberId);
        return R.ok().data("article",articleVo);

    }


    @ApiOperation(value = "根据标题关键字查询文章")
    @GetMapping("getAtcByKey/{keyWords}/{cnt}")
    public R getAtcByKey(@ApiParam(name = "keyWords", value = "文章标题关键字", required = true)
                                  @PathVariable String keyWords
                        ,@ApiParam(name = "cnt", value = "第几页", required = true)
                             @PathVariable long cnt) {
        Page<Article> pageParam = new Page<>(cnt, PageUtil.size);
        Map<String,Object> map =articleService.getArticleVoPage(keyWords,pageParam);

        return R.ok().data("map",map);

    }
    @ApiOperation(value = "根据标题关键字提示信息")
    @GetMapping("getWarnByKey/{keyWords}/{cnt}")
    public R getWarnByKey(@ApiParam(name = "keyWords", value = "文章标题关键字", required = true)
                                  @PathVariable String keyWords
                        ,@ApiParam(name = "cnt", value = "第几页", required = true)
                              @PathVariable long cnt) {
        Page<Article> pageParam = new Page<>(cnt, PageUtil.size);
        Map<String,Object> map =articleService.getWarnInfoByKey(keyWords,pageParam);
        return R.ok().data("map",map);

    }


    @ApiOperation(value = "查询推荐文章")
    @GetMapping("getArticles/{id}/{name}")
    public void getAtcIds(@PathVariable("id") String id
                                ,@PathVariable("name") String name) {

        articleService.getAtcId(id,name);

        commentService.updateComment(id,name);
    }
    @ApiOperation(value = "查询推荐文章")
    @PostMapping("getArticles1")
    public void getAtcIds1(@RequestBody UrlAndId urlAndId) {

        commentService.updateComment1(urlAndId.getId(),urlAndId.getSalt());
    }

    //增加文章
    @ApiOperation(value = "新增文章")
    @PostMapping("saveArticle")
    public R saveArticle(
            @ApiParam(name = "articleVo", value = "文章基本信息")
            ArticleVo articleVo
    ,@RequestPart MultipartFile[] files,HttpServletRequest request){
//        String memberId = "1";
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }


        String articleId = articleService.saveArticleInfo(articleVo,memberId);

        for (int i = 0; i < files.length; i++) {
            ossClient.uploadOssFile(articleId,files[i]);
//            ossClient.uploadOssFile(articleId,files);
        }
        if(!StringUtils.isEmpty(articleId)){
            return R.ok().data("articleId", articleId);
        }else{
            return R.error().message("保存失败");
        }
    }

    //删除文章
    @ApiOperation(value = "删除文章")
    @DeleteMapping("deleteArticle/{articleId}")
    public R deleteCourse(
            @ApiParam(name = "articleId", value = "文章ID", required = true)
            @PathVariable String articleId) {
        articleService.removeArticle(articleId);
        return R.ok();
    }

    //修改文章信息
    @ApiOperation(value = "修改文章")
    @PostMapping("updateArticleInfo")
    public R updateCourseInfo(@RequestBody ArticleVo articleVo) {
        articleService.updateArticleInfo(articleVo);
        return R.ok();
    }

    @GetMapping("getArticleCount")
    public String getArticleCount(@RequestParam String id){
        return articleService.countArticle(id).toString();
    }

    @GetMapping("getAtc/{cnt}")
    public R getAtc(HttpServletRequest request, @PathVariable("cnt") long cnt){
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        Page<Article> pageParam = new Page<>(cnt, PageUtil.size);
        Map<String,Object> map=articleService.getArticleInfo(memberId,pageParam);
        return R.ok().data("map",map);
    }
}

