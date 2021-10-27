package com.program.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.user.entity.User1User2;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-10-12
 */
public interface User1User2Mapper extends BaseMapper<User1User2> {

    Integer getCount(@Param("id") String id);
    Integer getCount1(@Param("id") String id);
}
