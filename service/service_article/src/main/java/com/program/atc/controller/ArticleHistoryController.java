package com.program.atc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.entity.Article;
import com.program.atc.entity.ArticleHistory;
import com.program.atc.service.ArticleHistoryService;
import com.program.commonutils.JwtUtils;
import com.program.commonutils.PageUtil;
import com.program.commonutils.R;
import com.program.security.security.TokenManager;
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
@RequestMapping("/ProgramPark/article/user/article-history")
//@CrossOrigin
public class ArticleHistoryController {

    @Autowired
    private ArticleHistoryService articleHistoryService;

    @Autowired
    private TokenManager tokenManager;

    @GetMapping("getAtc/{cnt}")
    public R getAtc(HttpServletRequest request, @PathVariable("cnt") long cnt){
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        List<ArticleHistory> articleIdList=new ArrayList<>();
        Page<Article> pageParam = new Page<>(cnt, PageUtil.size);
        articleIdList=articleHistoryService.selectByUserId1(memberId);
        Map<String,Object> map=articleHistoryService.selectByArticlesId(articleIdList,pageParam);
        return R.ok().data("map",map);
    }
}

