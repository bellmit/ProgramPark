package com.program.atc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.atc.client.OssClient;
import com.program.atc.entity.Article;
import com.program.atc.entity.ArticleHistory;
import com.program.atc.entity.vo.ArticleFrontVo;
import com.program.atc.entity.vo.ArticleVo;
import com.program.atc.mapper.ArticleHistoryMapper;
import com.program.atc.service.ArticleHistoryService;
import com.program.atc.service.ArticleService;
import com.program.atc.service.CommentService;
import com.program.commonutils.vo.UserFrontVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-10-12
 */
@Service
public class ArticleHistoryServiceImpl extends ServiceImpl<ArticleHistoryMapper, ArticleHistory> implements ArticleHistoryService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private OssClient ossClient;

    @Autowired
    private CommentService commentService;

    @Override
    public Map<String, Object> selectByArticlesId(List<ArticleHistory> userIdList,Page<Article> pageParam) {

        List<String> list=new ArrayList<>();
        for (int i = 0; i < userIdList.size(); i++) {
            list.add(userIdList.get(i).getAid());
        }
        List<Article> articleList=selectByUserId(list);


        List<ArticleFrontVo> articleFrontVoList=new ArrayList<>();

        for(int i=0;i<articleList.size();i++){
            Article article = articleList.get(i);

            ArticleFrontVo articleFrontVo=new ArticleFrontVo();
            BeanUtils.copyProperties(article,articleFrontVo);
            articleFrontVo.setPhotos(ossClient.getUrlsById(articleList.get(i).getId()).getList());
            articleFrontVo.setCommentCount(commentService.countComment1(articleList.get(i).getId()).toString());
            articleFrontVoList.add(articleFrontVo);

        }
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();//下一页
        boolean hasPrevious = pageParam.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("list", articleFrontVoList);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public List<Article> selectByUserId(List<String> usersId) {

        List<Article> articles=new ArrayList<>();

        for(int i=0;i<usersId.size();i++){
            Article article = articleService.getById(usersId.get(i));
            articles.add(article);
        }

        return articles;
    }

    @Override
    public List<ArticleHistory> selectByUserId1(String id) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("uid",id);
        wrapper.orderByDesc("gmt_create");
        List<ArticleHistory> list=baseMapper.selectList(wrapper);
        return list;
    }



    @Override
    public void saveArticleHistoryInfo(String articleId, String memberId) {

            //保存课程基本信息
        ArticleHistory articleHistory = new ArticleHistory();
        articleHistory.setAid(articleId);
        articleHistory.setUid(memberId);
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("aid",articleId);
        baseMapper.delete(wrapper);
        this.save(articleHistory);
    }
}
