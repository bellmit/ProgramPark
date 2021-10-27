package com.program.oss.service;

import com.program.oss.entity.PictureArticle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-14
 */
public interface PictureArticleService extends IService<PictureArticle> {

    void saveUrls(List<String> url,String aid);

    List<String> getUrls(String id);


}
