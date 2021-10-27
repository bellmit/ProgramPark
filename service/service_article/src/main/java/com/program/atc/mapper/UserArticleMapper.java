package com.program.atc.mapper;

import com.program.atc.entity.UserArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-10-20
 */
public interface UserArticleMapper extends BaseMapper<UserArticle> {
    Integer getCount(@Param("id") String id);
}
