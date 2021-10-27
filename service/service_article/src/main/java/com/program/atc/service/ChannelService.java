package com.program.atc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.entity.Article;
import com.program.atc.entity.Channel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-15
 */
public interface ChannelService extends IService<Channel> {

    List<Channel> getChannels();

}
