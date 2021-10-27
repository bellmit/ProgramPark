package com.program.atc.service;

import com.program.atc.entity.UserArticle;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-20
 */
public interface UserArticleService extends IService<UserArticle> {
    void Hand(String uid1,String aid2);

    Integer countHand(String id);

    Boolean isHand(String uid,String aid);
}
