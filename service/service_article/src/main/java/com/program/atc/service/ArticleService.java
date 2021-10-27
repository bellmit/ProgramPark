package com.program.atc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.program.atc.entity.vo.ArticleFrontVo;
import com.program.atc.entity.vo.ArticleVo;
import com.program.atc.entity.vo.ArticleWarnVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-09
 */
public interface ArticleService extends IService<Article> {
    //根据课程id查询课程基本信息
    Map<String, Object> getArticleInfo(Page<Article> pageParam, String channelId);
    Map<String, Object> getArticleInfo1(Page<Article> pageParam);

    void getAtcId(String id,String name);

    ArticleVo getArticleVo(String articleId,String id2);

    Map<String, Object> getArticleVoPage(String key,Page<Article> pageParam);

    Map<String,Object> getWarnInfoByKey(String key,Page<Article> pageParam);

    String saveArticleInfo(ArticleVo articleVo,String memberId);

    void updateCount(String cnt,String aid);

    //删除文章
    void removeArticle(String courseId);

    Integer countArticle(String id);

    //修改文章信息
    void updateArticleInfo(ArticleVo articleVo);

    Map<String, Object> getArticleInfo(String id,Page<Article> pageParam);

}
