package com.program.user.service.impl;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.security.entity.SecurityUser;
import com.program.security.entity.User;
import com.program.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * <p>
 * 自定义userDetailsService - 认证用户详情
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * 根据账号获取用户信息
     * @param username:
     * @return: org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 从数据库中取出用户信息
        User user = userService.selectByUsername(username);

        // 判断用户是否存在
        if (null == user){
            User user3 = new User();
            user3.setUsername(username);
            user3.setEmail(username);
            String code =  (redisTemplate.opsForValue().get(username+"nimabi")).toString();

            user3.setPassword(code);
            userService.save(user3);
            user=user3;
        }else{

        }
        // 返回UserDetails实现类
//        com.program.security.entity.User curUser = new com.program.security.entity.User();
        User user2=new User();
        user2.setUsername(user.getEmail());
        user2.setEmail(user.getEmail());
        user2.setPassword(redisTemplate.opsForValue().get(user.getEmail()+"nimabi").toString());
        user2.setId(user.getId());
        SecurityUser user1=new SecurityUser(user2,null);
//        BeanUtils.copyProperties(user,user1);
        ArrayList<String> objects = new ArrayList<>();
        objects.add("role");
        user1.setPermissionValueList(objects);
//        SecurityUser securityUser = new SecurityUser(curUser);
//        List<GrantedAuthority> authorities = ;
        return user1;
    }

}
