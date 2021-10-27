package com.program.atc.mapper;

import com.program.atc.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.atc.entity.vo.ArticleFrontVo;
import com.program.atc.entity.vo.ArticleWarnVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-10-09
 */
public interface ArticleMapper extends BaseMapper<Article> {
//    List<ArticleFrontVo> selectAtcByKey(@Param("key")String key);
//    List<ArticleWarnVo> selectWarnByKey(@Param("key")String key);
Integer getCount(@Param("id") String id);

}
