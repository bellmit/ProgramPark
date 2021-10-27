package com.program.atc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.program.atc.entity.Article;
import com.program.atc.entity.ArticleHistory;
import com.program.atc.entity.vo.ArticleVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-12
 */
public interface ArticleHistoryService extends IService<ArticleHistory> {

    Map<String, Object> selectByArticlesId(List<ArticleHistory> articlesId, Page<Article> pageParam);

    List<Article> selectByUserId(List<String> usersId);

    List<ArticleHistory> selectByUserId1(String id);

    void saveArticleHistoryInfo(String articleId, String memberId);

}
