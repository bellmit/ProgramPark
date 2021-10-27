package com.program.atc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.client.OssClient;
import com.program.atc.entity.Article;
import com.program.atc.entity.UserArticle;
import com.program.atc.entity.vo.ArticleFrontVo;
import com.program.atc.service.ArticleService;
import com.program.atc.entity.User1Article2;
import com.program.atc.mapper.User1Article2Mapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.atc.service.CommentService;
import com.program.atc.service.User1Article2Service;
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
public class User1Article2ServiceImpl extends ServiceImpl<User1Article2Mapper, User1Article2> implements User1Article2Service {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private User1Article2Mapper user1Article2Mapper;

    @Autowired
    private OssClient ossClient;

    @Autowired
    private CommentService commentService;

    @Override
    public List<Article> selectByUserId(List<String> articlesId) {

        List<Article> articles=new ArrayList<>();

        for(int i=0;i<articlesId.size();i++){
            Article article = articleService.getById(articlesId.get(i));
            articles.add(article);
        }

        return articles;
    }



    @Override
    public Map<String, Object> selectByUserId(Page<Article> pageParam, List<String> userIdList) {

        List<Article> articleList=selectByUserId(userIdList);

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
        map.put("items", articleFrontVoList);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public Boolean isCollection(String aid, String uid) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("uid",uid);
        wrapper.eq("aid",aid);
        Integer res=baseMapper.selectCount(wrapper);
        if(res==1){
            return true;
        }
        return false;
    }

    @Override
    public void Collect(String uid1, String aid2) {
            User1Article2 user1Article2=new User1Article2();

            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("uid",uid1);
            wrapper.eq("aid",aid2);
            int count=user1Article2Mapper.selectCount(wrapper);
            if(count==1){
                user1Article2Mapper.delete(wrapper);
            }else{
                user1Article2.setUid(uid1);
                user1Article2.setAid(aid2);
                user1Article2Mapper.insert(user1Article2);
            }
    }
}
