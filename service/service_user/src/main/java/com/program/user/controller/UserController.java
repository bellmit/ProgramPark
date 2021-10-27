package com.program.user.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.entity.vo.ArticleVo;
import com.program.commonutils.PageUtil;
import com.program.commonutils.R;
import com.program.commonutils.vo.SaltVo;
import com.program.security.entity.User;
import com.program.commonutils.vo.UserFrontVo;
import com.program.security.security.TokenManager;
import com.program.user.client.ArticleClient;
import com.program.user.client.OssClient;
import com.program.user.entity.vo.UserInfoVo;
import com.program.user.entity.vo.UserVo;
import com.program.user.mapper.User1User2Mapper;
import com.program.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-10
 */
@RestController
@RequestMapping("/ProgramPark/user")
//@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private User1User2Mapper user1User2Mapper;

    @Autowired
    private ArticleClient articleClient;

    @Autowired
    private OssClient ossClient;

    @Autowired
    private TokenManager tokenManager;

    @GetMapping("getUserById/{id}")
    public UserFrontVo getUserById(@PathVariable("id") String id){
        User user = userService.getById(id);
        UserFrontVo userFrontVo=new UserFrontVo();
        BeanUtils.copyProperties(user,userFrontVo);
        return userFrontVo;
    }

    @GetMapping("getUser")
    public R getUserById(HttpServletRequest request){
//        String memberId = "1";
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        User user = userService.getById(memberId);

        Integer count1=userService.countAttention(memberId);
        String count2= articleClient.getOneComment(memberId);
        String count3= articleClient.getArticleCount(memberId);
        Integer count4= user1User2Mapper.getCount1(memberId);
        UserVo userVo=new UserVo();
        BeanUtils.copyProperties(user,userVo);
        userVo.setAttentionCount(count1.toString());
        userVo.setFanCount(count4.toString());
        userVo.setCommentCount(count2.toString());
        userVo.setArticleCount(count3.toString());
        userVo.setHands(articleClient.getHandCount(memberId));
        return R.ok().data("user",userVo);
    }

    @GetMapping("getUsers/{page}")
    public R getUsers(@ApiParam(name = "page", value = "第几页", required = true)
                      @PathVariable long page
            , HttpServletRequest request){
        Page<UserFrontVo> pageParam = new Page<>(page, PageUtil.size);
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        List<String> articleIdList=new ArrayList<>();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("uid1",memberId);
        articleIdList=user1User2Mapper.selectList(wrapper);
        Map<String,Object> map =userService.getUsers(pageParam,articleIdList);
        return R.ok().data("map",map);
    }

    //修改文章信息
    @ApiOperation(value = "修改个人信息")
    @PostMapping(value = "updateUserInfo")
    public R updateUserInfo(HttpServletRequest request,@RequestBody Map<String,Object> param) {
        UserInfoVo userInfoVo=new UserInfoVo();
        userInfoVo.setUsername((String)param.get("username"));
//        String memberId = tokenManager.getMemberIdByJwtToken(request);
//        String memberId = "1";
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        userService.updateUserInfo(userInfoVo,memberId);
        articleClient.getAtcIds(memberId,userInfoVo.getUsername());
        return R.ok();
    }
    //修改文章信息
    @ApiOperation(value = "修改头像信息")
    @PostMapping("updateSaltById")
    public void updateSaltById(@RequestBody SaltVo saltVo){
        userService.updateSalt(saltVo);

    }
    @GetMapping("FanById/{id}")
    public R FanById(@PathVariable("id") String id,HttpServletRequest request){
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        if(memberId.equals(id)){
            return R.error().code(28005).message("不能关注自己");
        }
        User user=new User();
        user.setId(id);
        userService.Fan(id,memberId);
        return R.ok();
    }

    @ApiOperation(value = "是否关注")
    @PostMapping("isAttention/{id1}/{id2}")
    public String isAttention(@PathVariable("id1") String id1,@PathVariable("id2") String id2){
        if(id1.equals(id2)){
            return "0";
        }
        if(userService.isAttention(id1,id2)){
            return "1";
        }
        return "0";
    }
}

