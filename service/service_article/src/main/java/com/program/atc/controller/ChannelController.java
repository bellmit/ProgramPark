package com.program.atc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.entity.Article;
import com.program.atc.entity.Channel;
import com.program.atc.service.ChannelService;
import com.program.commonutils.PageUtil;
import com.program.commonutils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-15
 */
@RestController
@RequestMapping("/ProgramPark/article/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @GetMapping("getChannels")
    public R getAtc(){
        List<Channel> list=channelService.getChannels();
        return R.ok().data("list",list);
    }

}

