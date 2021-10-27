package com.program.atc.mapper;

import com.program.atc.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-10-09
 */
public interface CommentMapper extends BaseMapper<Comment> {

    Integer getCount(@Param("id") String id);
    Integer getCount1(@Param("id") String id);

}
