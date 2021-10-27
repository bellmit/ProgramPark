package com.program.atc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.program.atc.entity.User1Article2;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2021-10-12
 */
public interface User1Article2Mapper extends BaseMapper<User1Article2> {

    List<String> getIdById(String id);
}
