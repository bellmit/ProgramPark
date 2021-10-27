package com.program.atc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.program.atc.entity.UserArticle;
import com.program.atc.mapper.UserArticleMapper;
import com.program.atc.service.UserArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-10-20
 */
@Service
public class UserArticleServiceImpl extends ServiceImpl<UserArticleMapper, UserArticle> implements UserArticleService {

    @Autowired
    private UserArticleMapper userArticleMapper;


    @Override
    public void Hand(String uid1,String aid2) {
        UserArticle userArticle=new UserArticle();

        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("uid1",uid1);
        wrapper.eq("aid2",aid2);
        int count=userArticleMapper.selectCount(wrapper);
        if(count==1){
            userArticleMapper.delete(wrapper);
        }else{
            userArticle.setUid1(uid1);
            userArticle.setAid2(aid2);
            userArticleMapper.insert(userArticle);
        }
    }

    @Override
    public Boolean isHand(String uid, String aid) {
        if(uid==null) return false;
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("aid2",aid);
        wrapper.eq("uid1",uid);
        int cnt=baseMapper.selectCount(wrapper);
        if(cnt==1) return true;
        return false;
    }

    @Override
    public Integer countHand(String id) {

        return userArticleMapper.getCount(id);
    }
}
