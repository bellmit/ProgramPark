package com.program.atc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.client.OssClient;
import com.program.atc.client.UserClient;
import com.program.atc.entity.Article;
import com.program.atc.entity.Comment;
import com.program.atc.entity.vo.ArticleFrontVo;
import com.program.atc.entity.vo.ArticleVo;
import com.program.atc.entity.vo.ArticleWarnVo;
import com.program.atc.mapper.ArticleMapper;
import com.program.atc.mapper.CommentMapper;
import com.program.atc.mapper.User1Article2Mapper;
import com.program.atc.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.atc.service.CommentService;
import com.program.atc.service.User1Article2Service;
import com.program.atc.service.UserArticleService;
import com.program.commonutils.vo.UserFrontVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
 * @since 2021-10-09
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private OssClient ossClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private CommentService commentService;

    @Autowired
    private User1Article2Service user1Article2Service;

    @Autowired
    private UserArticleService userArticleService;


    @Override
    public Map<String, Object> getArticleInfo(Page<Article> pageParam,String channelId) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("channel_id",channelId).orderByDesc("length(comment_count),comment_count").orderByDesc("gmt_create");
        articleMapper.selectPage(pageParam,wrapper);

        List<Article> articleList=pageParam.getRecords();
        List<ArticleFrontVo> articleFrontVoList=new ArrayList<>();

        for (int i = 0; i < articleList.size() ; i++) {
            Article article=articleList.get(i);
            ArticleFrontVo articleFrontVo=new ArticleFrontVo();
            articleFrontVo.setPhotos(ossClient.getUrlsById(articleList.get(i).getId()).getList());
            articleFrontVo.setCommentCount(commentService.countComment1(articleList.get(i).getId()).toString());
            BeanUtils.copyProperties(article,articleFrontVo);
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
        map.put("items", articleFrontVoList);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }
    @Override
    public Map<String, Object> getArticleInfo1(Page<Article> pageParam) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("length(comment_count),comment_count").orderByDesc("gmt_create");

        articleMapper.selectPage(pageParam,wrapper);

        List<Article> articleList=pageParam.getRecords();
        List<ArticleFrontVo> articleFrontVoList=new ArrayList<>();

        for (int i = 0; i < articleList.size() ; i++) {
            Article article=articleList.get(i);
            ArticleFrontVo articleFrontVo=new ArticleFrontVo();
            articleFrontVo.setPhotos(ossClient.getUrlsById(articleList.get(i).getId()).getList());
            articleFrontVo.setCommentCount(commentService.countComment1(articleList.get(i).getId()).toString());
            BeanUtils.copyProperties(article,articleFrontVo);
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
        map.put("items", articleFrontVoList);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }

    @Override
    public void getAtcId(String id,String name) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("author_id",id);
        List<Article> list = baseMapper.selectList(wrapper);
        List<String> list1=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list1.add(list.get(i).getId());
        }
        for (int i = 0; i < list1.size(); i++) {
            Article article=new Article();
            article.setId(list1.get(i));
            article.setAuthorName(name);
            baseMapper.updateById(article);
        }

    }

    @Override
    public ArticleVo getArticleVo(String articleId,String id2) {
        Article article=baseMapper.selectById(articleId);
        ArticleVo articleVo=new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);
        String id1=article.getAuthorId();
        String res= userClient.isAttention(id1,id2);
        articleVo.setAuthorAveter(userClient.getUserById(id1).getSalt());
        if(id1.equals(res)){
            articleVo.setIsAttention(true);
        }else{
            articleVo.setIsAttention(false);
        }
        Boolean res1= user1Article2Service.isCollection(articleId,id2);
        if(res1){
            articleVo.setIsCollection(true);
        }else{
            articleVo.setIsCollection(false);
        }
        Boolean res2= userArticleService.isHand(id2,articleId);
        if(res2){
            articleVo.setIsLike(true);
        }else{
            articleVo.setIsLike(false);
        }
        articleVo.setCommentCount(commentService.countComment1(article.getId()).toString());
        return articleVo;
    }

    @Override
    public Map<String, Object> getArticleVoPage(String key,Page<Article> pageParam) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.like("title",key);
        wrapper.orderByDesc("gmt_create").orderByDesc("length(comment_count),comment_count");
        articleMapper.selectPage(pageParam,wrapper);

        List<Article> articleList=pageParam.getRecords();
        List<ArticleFrontVo> articleVoList=new ArrayList<>();

        for (int i = 0; i < articleList.size() ; i++) {
            Article article=articleList.get(i);
            ArticleFrontVo articleFrontVo=new ArticleFrontVo();
            BeanUtils.copyProperties(article,articleFrontVo);
            articleFrontVo.setPhotos(ossClient.getUrlsById(articleList.get(i).getId()).getList());
            articleFrontVo.setCommentCount(commentService.countComment1(articleList.get(i).getId()).toString());
            articleVoList.add(articleFrontVo);
        }
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();//下一页
        boolean hasPrevious = pageParam.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", articleVoList);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }


    @Override
    public Map<String,Object> getWarnInfoByKey(String key,Page<Article> pageParam) {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.like("title",key);
        wrapper.orderByDesc("gmt_create");
        articleMapper.selectPage(pageParam,wrapper);

        List<Article> articleList=pageParam.getRecords();
        List<ArticleWarnVo> articleWarnVoList=new ArrayList<>();

        for (int i = 0; i < articleList.size() ; i++) {
            Article article=articleList.get(i);
            ArticleWarnVo articleWarnVo=new ArticleWarnVo();
            BeanUtils.copyProperties(article,articleWarnVo);
            articleWarnVoList.add(articleWarnVo);
        }
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();//下一页
        boolean hasPrevious = pageParam.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("list", articleWarnVoList);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }

    @Override
    public Map<String, Object> getArticleInfo(String id,Page<Article> pageParam) {

        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("author_id",id).orderByDesc("gmt_create");
        articleMapper.selectPage(pageParam,wrapper);
        List<Article> articleList=pageParam.getRecords();
        List<ArticleFrontVo> articleFrontVoList=new ArrayList<>();
        for (int i = 0; i < articleList.size() ; i++) {
            Article article=articleList.get(i);
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
    public String saveArticleInfo(ArticleVo articleVo,String memberId) {

        UserFrontVo userById = userClient.getUserById(memberId);
        //保存课程基本信息
        Article article = new Article();
        BeanUtils.copyProperties(articleVo, article);
//        boolean resultCourseInfo = this.save(article);

        article.setStatus(Article.COURSE_DRAFT);
        article.setAuthorId(memberId);
        article.setAuthorName(userById.getUsername());
        this.save(article);
//        if(!resultCourseInfo){
//            throw new GuliException(20001, "课程信息保存失败");
//        }


//        if(!resultDescription){
//            throw new GuliException(20001, "课程详情信息保存失败");
//        }

        return article.getId();
    }

    @Override
    public void updateCount(String cnt,String aid) {
        Article article=new Article();
        article.setId(aid);
        article.setCommentCount(cnt);
        baseMapper.updateById(article);
    }

    //删除课程
    @Override
    public void removeArticle(String articleId) {
        //1 根据课程id删除小节
        commentService.removeComment(articleId);

        //删图片！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！

        //3 根据课程id删除描述
        articleMapper.deleteById(articleId);

        //4 根据课程id删除课程本身
        int result = baseMapper.deleteById(articleId);
//        if(result == 0) { //失败返回
//            throw new GuliException(20001,"删除失败");
//        }
    }

    @Override
    public void updateArticleInfo(ArticleVo articleVo) {
        //1 修改课程表
        Article article = new Article();
        BeanUtils.copyProperties(articleVo, article);
        int update = baseMapper.updateById(article);
//        if(update == 0) {
//            throw new GuliException(20001,"修改课程信息失败");
//        }

    }

    @Override
    public Integer countArticle(String id) {

        return articleMapper.getCount(id);
    }
}
