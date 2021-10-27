package com.program.atc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.entity.Comment;
import com.program.atc.entity.comment.TwoComment;
import com.program.atc.service.CommentService;
import com.program.commonutils.PageUtil;
import com.program.commonutils.R;
import com.program.commonutils.vo.CommentVo;
import com.program.security.security.TokenManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-10-09
 */
@RestController
@RequestMapping("/ProgramPark/article/comment")
//@CrossOrigin
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("saveComment")
    public R saveComment(@RequestBody CommentVo commentVo,HttpServletRequest request){
        String userId = tokenManager.getMemberIdByJwtToken(request);
//        String userId = "4";
        if(StringUtils.isEmpty(userId)) {
            return R.error().code(28004).message("请登录");
        }
        commentService.saveComment(commentVo,userId,commentVo.getId1(),commentVo.getId2(),commentVo.getId3(),commentVo.getId4());
        return R.ok();
    }

    @GetMapping("getTwoComment/{id}/{cnt}")
    public R getTwoComment(@PathVariable("id") String id,@PathVariable("cnt") long cnt){
        Page<Comment> page = new Page<>(cnt, PageUtil.size);

        Map<String,Object> map=commentService.getTwoCommentPage(page,id);
        return R.ok().data("map",map);
    }
    @GetMapping("getOneComment/{id}/{cnt}")
    public R getOneComment(@PathVariable("id") String id,@PathVariable("cnt") long cnt){
        Page<Comment> page = new Page<>(cnt, PageUtil.size);

        Map<String,Object> map=commentService.getOneCommentPage(page,id);
        return R.ok().data("map",map);
    }

    @GetMapping("getCommentFrontPage/{cnt}")
    public R getCommentFrontPage(@PathVariable("cnt") long cnt,HttpServletRequest request){
        String userId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(userId)) {
            return R.error().code(28004).message("请登录");
        }
        Page<Comment> page = new Page<>(cnt, PageUtil.size);
        Map<String,Object> map=commentService.getCommentFrontPage(page,userId);
        return R.ok().data("map",map);
    }

    @GetMapping("getCommentCount")
    public String getOneComment(@RequestParam String id){
        return commentService.countComment(id).toString();
    }
    @GetMapping("getCommentCount1")
    public String getOneComment1(@RequestParam String id){
        return commentService.countComment1(id).toString();
    }
}

