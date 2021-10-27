package com.program.atc.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.program.atc.client.UserClient;
import com.program.atc.entity.Article;
import com.program.atc.entity.Comment;
import com.program.atc.entity.comment.OneComment;
import com.program.atc.entity.comment.TwoComment;
import com.program.atc.entity.vo.ArticleVo;
import com.program.atc.entity.vo.CommentFrontVo;
import com.program.atc.mapper.CommentMapper;
import com.program.atc.service.ArticleService;
import com.program.atc.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.commonutils.vo.CommentVo;
import com.program.commonutils.vo.UserFrontVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-10-09
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleService articleService;

    @Override
    public Map<String, Object> getOneCommentPage(Page<Comment> pageParam1, String articleId) {
        QueryWrapper<Comment> wrapper1 = new QueryWrapper<>();

        wrapper1.eq("article_id",articleId);

        wrapper1.eq("parent_id","0");
        wrapper1.orderByDesc("gmt_create");
//        if (!StringUtils.isEmpty((CharSequence) articleVo.getGmtCreate())) { //最新
//        }

        baseMapper.selectPage(pageParam1,wrapper1);

        List<Comment> oneCommentList = pageParam1.getRecords();

        List<OneComment> finalCommentList=new ArrayList<>();
        for (int i = 0; i < oneCommentList.size(); i++) {
            OneComment oneComment=new OneComment();
            BeanUtils.copyProperties(oneCommentList.get(i),oneComment);
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("parent_id",oneCommentList.get(i).getId());
            oneComment.setChildrenCnt(commentMapper.selectCount(wrapper).toString());
            finalCommentList.add(oneComment);
        }

        long current1 = pageParam1.getCurrent();
        long pages1 = pageParam1.getPages();
        long size1 = pageParam1.getSize();
        long total1 = pageParam1.getTotal();
        boolean hasNext1 = pageParam1.hasNext();//下一页
        boolean hasPrevious1 = pageParam1.hasPrevious();//上一页
        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("list", finalCommentList);
        map.put("current1", current1);
        map.put("pages1", pages1);
        map.put("size1", size1);
        map.put("total", total1);
        map.put("hasNext1", hasNext1);
        map.put("hasPrevious1", hasPrevious1);
        //map返回
        return map;
    }
    @Override
    public Map<String, Object> getCommentFrontPage(Page<Comment> pageParam1, String id) {
        QueryWrapper<Comment> wrapper1 = new QueryWrapper<>();

        wrapper1.eq("id1",id);
        wrapper1.orderByDesc("gmt_create");

//        if (!StringUtils.isEmpty((CharSequence) articleVo.getGmtCreate())) { //最新
//        }

        baseMapper.selectPage(pageParam1,wrapper1);

        List<Comment> oneCommentList = pageParam1.getRecords();

        List<TwoComment> finalCommentList=new ArrayList<>();
        for (int i = 0; i < oneCommentList.size(); i++) {
            TwoComment twoComment=new TwoComment();
            BeanUtils.copyProperties(oneCommentList.get(i),twoComment);
            twoComment.setOriginUsername(userClient.getUserById(id).getUsername());
            twoComment.setTitle(articleService.getArticleVo(oneCommentList.get(i).getArticleId(),"-1").getTitle());
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("id",oneCommentList.get(i).getId());

            twoComment.setContent1(commentMapper.selectOne(wrapper).getContent());
            finalCommentList.add(twoComment);
        }

        long current1 = pageParam1.getCurrent();
        long pages1 = pageParam1.getPages();
        long size1 = pageParam1.getSize();
        long total1 = pageParam1.getTotal();
        boolean hasNext1 = pageParam1.hasNext();//下一页
        boolean hasPrevious1 = pageParam1.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("list", finalCommentList);
        map.put("current1", current1);
        map.put("pages1", pages1);
        map.put("size1", size1);
        map.put("total", total1);
        map.put("hasNext1", hasNext1);
        map.put("hasPrevious1", hasPrevious1);
        //map返回
        return map;
    }

    @Override
    public Map<String, Object> getTwoCommentPage(Page<Comment> pageParam1, String id) {
        QueryWrapper<Comment> wrapper1 = new QueryWrapper<>();

        wrapper1.eq("parent_id",id);
        wrapper1.orderByDesc("gmt_create");


        baseMapper.selectPage(pageParam1,wrapper1);

        List<Comment> oneCommentList = pageParam1.getRecords();

        List<TwoComment> finalCommentList=new ArrayList<>();
        for (int i = 0; i < oneCommentList.size(); i++) {
            if("".equals(oneCommentList.get(i).getId1())||oneCommentList.get(i).getId1()==null) continue;
            TwoComment twoComment=new TwoComment();
            BeanUtils.copyProperties(oneCommentList.get(i),twoComment);
            QueryWrapper<Comment> wrapper = new QueryWrapper<>();
            wrapper.eq("id",oneCommentList.get(i).getId2());
            twoComment.setContent1(baseMapper.selectOne(wrapper).getContent());
            twoComment.setOriginUsername(userClient.getUserById(oneCommentList.get(i).getId1()).getUsername());
            twoComment.setTitle(articleService.getArticleVo(oneCommentList.get(i).getArticleId(),"-1").getTitle());
            finalCommentList.add(twoComment);
        }

        long current1 = pageParam1.getCurrent();
        long pages1 = pageParam1.getPages();
        long size1 = pageParam1.getSize();
        long total1 = pageParam1.getTotal();
        boolean hasNext1 = pageParam1.hasNext();//下一页
        boolean hasPrevious1 = pageParam1.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("list", finalCommentList);
        map.put("current1", current1);
        map.put("pages1", pages1);
        map.put("size1", size1);
        map.put("total", total1);
        map.put("hasNext1", hasNext1);
        map.put("hasPrevious1", hasPrevious1);
        //map返回
        return map;
    }

    @Override
    public Integer countComment(String id) {

        return commentMapper.getCount(id);
    }
    @Override
    public Integer countComment1(String id) {

        return commentMapper.getCount1(id);
    }

    @Override
    public void updateComment(String id, String name) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("user_id",id);
        List<Comment> list = baseMapper.selectList(wrapper);
        List<String> list1=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list1.add(list.get(i).getId());
        }
        for (int i = 0; i < list1.size(); i++) {
            Comment comment=new Comment();
            comment.setId(list1.get(i));
            comment.setUsername(name);
            baseMapper.updateById(comment);
        }
    }
    @Override
    public void updateComment1(String id, String salt) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("user_id",id);
        List<Comment> list = baseMapper.selectList(wrapper);
        List<String> list1=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list1.add(list.get(i).getId());
        }
        for (int i = 0; i < list1.size(); i++) {
            Comment comment=new Comment();
            comment.setId(list1.get(i));
            comment.setAvatar(salt);
            baseMapper.updateById(comment);
        }
    }

    @Override
    public void removeComment(String commentId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("id",commentId);
        baseMapper.delete(wrapper);
    }

    @Override
    public void saveComment(CommentVo commentVo,String userId,String commentId
            ,String articleId,String authorId,String parentId) {
        Comment comment=new Comment();
        BeanUtils.copyProperties(commentVo,comment);

        UserFrontVo userFrontVo=userClient.getUserById(userId);

        comment.setArticleId(articleId);

        comment.setParentId(parentId);

        comment.setAuthorId(authorId);

        comment.setId2(commentId);

        comment.setUserId(userId);

        if(commentVo.getId1()==null||"".equals(commentVo.getId1())){
            comment.setId1(authorId);
        }else {
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("id",commentId);
            Comment comment1=getOne(wrapper);
            comment.setId1(comment1.getUserId());
        }

        comment.setAvatar(userFrontVo.getSalt());

        comment.setUsername(userFrontVo.getUsername());

        Integer tmp=countComment1(articleId)+1;

        articleService.updateCount(tmp.toString(),articleId);

        baseMapper.insert(comment);

    }


}
