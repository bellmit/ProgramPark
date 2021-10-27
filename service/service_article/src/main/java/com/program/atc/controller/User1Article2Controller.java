package com.program.atc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.entity.Article;
import com.program.atc.mapper.User1Article2Mapper;
import com.program.atc.service.User1Article2Service;
import com.program.commonutils.PageUtil;
import com.program.commonutils.R;
import com.program.security.entity.User;
import com.program.security.security.TokenManager;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-12
 */
@RestController
@RequestMapping("/ProgramPark/article/user1-article2")
//@CrossOrigin
public class User1Article2Controller {
    @Autowired
    private User1Article2Service user1Article2Service;

    @Autowired
    private User1Article2Mapper user1Article2Mapper;

    @Autowired
    private TokenManager tokenManager;

    @GetMapping("getAtc/{page}")
    public R getAtc(@ApiParam(name = "page", value = "第几页", required = true)
                        @PathVariable long page,
                    HttpServletRequest request){
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }


        Page<Article> pageArticle = new Page<>(page, PageUtil.size);


        Map<String,Object> map = user1Article2Service.selectByUserId(pageArticle
                ,user1Article2Mapper.getIdById(memberId));
        return R.ok().data("map",map);
    }

    @GetMapping("Collect/{id}")
    public R Hand(@PathVariable("id") String id, HttpServletRequest request){
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        if(memberId.equals(id)){
            return R.error().code(28005).message("不能收藏自己");
        }
        User user=new User();
        user.setId(id);
        user1Article2Service.Collect(memberId,id);
        return R.ok();
    }
}

