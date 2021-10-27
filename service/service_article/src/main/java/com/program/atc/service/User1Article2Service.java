package com.program.atc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.entity.Article;
import com.program.atc.entity.User1Article2;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface User1Article2Service extends IService<User1Article2> {

    List<Article> selectByUserId(List<String> articlesId);

    Map<String, Object> selectByUserId(Page<Article> pageParam, List<String> userIdList);

    Boolean isCollection(String aid,String uid);

    void Collect(String uid1,String aid2);
}
