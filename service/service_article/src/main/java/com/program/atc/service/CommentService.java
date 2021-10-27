package com.program.atc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.program.atc.entity.comment.TwoComment;
import com.program.atc.entity.vo.ArticleVo;
import com.program.commonutils.vo.CommentVo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2021-10-09
 */
public interface CommentService extends IService<Comment> {
    public Map<String, Object> getOneCommentPage(Page<Comment> pageParam1,String articleId);

    void removeComment(String commentId);

    void saveComment(CommentVo commentVo, String userId, String commentId
            , String articleId, String authorId, String parentId);

    Map<String, Object> getCommentFrontPage(Page<Comment> pageParam1, String id);

    Map<String,Object> getTwoCommentPage(Page<Comment> page,String id);

    Integer countComment(String id);
    Integer countComment1(String id);

    void updateComment(String id,String name);
    void updateComment1(String id,String salt);

}
