package com.program.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.atc.client.OssClient;
import com.program.commonutils.JwtUtils;
import com.program.commonutils.vo.SaltVo;
import com.program.commonutils.vo.UserFrontVo;
import com.program.security.entity.User;
import com.program.user.client.ArticleClient;
import com.program.user.entity.User1User2;
import com.program.user.entity.vo.UserInfoVo;
import com.program.user.mapper.User1User2Mapper;
import com.program.user.mapper.UserMapper;
import com.program.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
 * @since 2021-10-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private OssClient ossClient;

    @Autowired
    private ArticleClient articleClient;

    @Autowired
    private User1User2Mapper user1User2Mapper;

    @Override
    public User selectByUsername(String username) {

        return baseMapper.selectOne(new QueryWrapper<User>().eq("email", username));
    }

    @Override
    public User getUserById(String id) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("id", id));
    }

    @Override
    public String loginUser(String email, String code) {
        if(code.equals(redisTemplate.opsForValue().get(email))){
            return JwtUtils.getJwtToken(email, code);
        }
        return null;
    }

    @Override
    public List<User> selectByUserId(List<String> articlesId) {

        List<User> users=new ArrayList<>();

        for(int i=0;i<articlesId.size();i++){
            User user = userService.getById(articlesId.get(i));
            users.add(user);
        }

        return users;
    }

    @Override
    public Map<String, Object> getUsers(Page<UserFrontVo> pageParam, List<String> userIdList) {

        List<User> userIds=selectByUserId(userIdList);

        List<UserFrontVo> userFrontVoList=new ArrayList<>();

        for (int i = 0; i < userIds.size() ; i++) {
            User user=userIds.get(i);
            UserFrontVo userFrontVo=new UserFrontVo();
            BeanUtils.copyProperties(user,userFrontVo);
            userFrontVoList.add(userFrontVo);
        }

        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();//下一页
        boolean hasPrevious = pageParam.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", userFrontVoList);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public Integer countAttention(String id) {

        return user1User2Mapper.getCount(id);
    }
    @Override
    public Integer countFan(String id) {

        return user1User2Mapper.getCount1(id);
    }

    @Override
    public void updateUserInfo(UserInfoVo userInfoVo, String id) {
        User user=new User();
        BeanUtils.copyProperties(userInfoVo,user);
        user.setId(id);
//        if(update == 0) {
//            throw new GuliException(20001,"修改课程信息失败");
//        }


        int update = baseMapper.updateById(user);

    }
    @Override
    public void updateSalt(SaltVo saltVo) {
        User user=new User();
        user.setId(saltVo.getId());
        user.setSalt(saltVo.getUrl());
        baseMapper.updateById(user);
    }

    @Override
    public Boolean isAttention(String uid1,String uid2) {
            if(uid2==null||uid1==null) return false;
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("uid2",uid2);
            wrapper.eq("uid1",uid1);
            int cnt=user1User2Mapper.selectCount(wrapper);
            if(cnt==1) return true;
            return false;
    }

    @Override
    public void Fan(String uid1,String uid2) {
        User1User2 user1User2=new User1User2();

        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("uid1",uid1);
        wrapper.eq("uid2",uid2);
        int count=user1User2Mapper.selectCount(wrapper);
        if(count==1){
            user1User2Mapper.delete(wrapper);
        }else{
            user1User2.setUid1(uid1);
            user1User2.setUid2(uid2);
            user1User2Mapper.insert(user1User2);
        }
    }

}
