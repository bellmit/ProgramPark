package com.program.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.oss.entity.PictureArticle;
import com.program.oss.mapper.PictureArticleMapper;
import com.program.oss.service.PictureArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-10-14
 */
@Service
public class PictureArticleServiceImpl extends ServiceImpl<PictureArticleMapper, PictureArticle> implements PictureArticleService {

    @Override
    public void saveUrls(List<String> urls,String aid) {
        for (int i = 0; i < urls.size(); i++) {
            PictureArticle pictureArticle=new PictureArticle();
            pictureArticle.setAid(aid);
            pictureArticle.setUrl(urls.get(i));
            baseMapper.insert(pictureArticle);
        }
    }

    @Override
    public List<String> getUrls(String id) {
        List<String> list=new ArrayList<>();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("aid",id);
        List<PictureArticle> list1=baseMapper.selectList(wrapper);
        for (int i = 0; i < list1.size(); i++) {
            list.add(list1.get(i).getUrl());
        }
        return list;
    }
}
