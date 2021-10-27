package com.program.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.program.commonutils.vo.SaltVo;
import com.program.commonutils.vo.UserFrontVo;
import com.program.security.entity.User;
import com.program.user.entity.vo.UserInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-10
 */
public interface UserService extends IService<User> {
    // 从数据库中取出用户信息
    User selectByUsername(String username);

    User getUserById(String id);

    void Fan(String uid1,String uid2);

    List<User> selectByUserId(List<String> usersId);

    String loginUser(String email,String code);

    Map<String, Object> getUsers(Page<UserFrontVo> pageParam, List<String> userIdList);

    Integer countAttention(String id);
    Integer countFan(String id);

    void updateUserInfo(UserInfoVo userInfoVo,String id);

    void updateSalt(SaltVo saltVo);

    Boolean isAttention(String uid1,String uid2);
}
