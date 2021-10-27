package com.program.atc.controller;


import com.program.atc.service.UserArticleService;
import com.program.commonutils.R;
import com.program.security.entity.User;
import com.program.security.security.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-20
 */
@RestController
@RequestMapping("/ProgramPark/article")
public class UserArticleController {

    @Autowired
    TokenManager tokenManager;

    @Autowired
    UserArticleService userArticleService;

    @GetMapping("Hand/{id}")
    public R Hand(@PathVariable("id") String id, HttpServletRequest request){
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        if(memberId.equals(id)){
            return R.error().code(28005).message("不能给自己点赞");
        }
        User user=new User();
        user.setId(id);
        userArticleService.Hand(memberId,id);
        return R.ok();
    }

    @GetMapping("getHandCount")
    public String getHandCount(@RequestParam String id){
        return userArticleService.countHand(id).toString();
    }
}

