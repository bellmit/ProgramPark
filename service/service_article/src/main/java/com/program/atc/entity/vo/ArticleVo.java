package com.program.atc.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ArticleVo {
    @ApiModelProperty(value = "文章ID")
    private String id;
    @ApiModelProperty(value = "作者id")
    private String authorId;
    @ApiModelProperty(value = "作者名称")
    private String authorName;
    @ApiModelProperty(value = "作者头像")
    private String authorAveter;
    @ApiModelProperty(value = "当前用户是否关注该作者")
    private Boolean isAttention;
    @ApiModelProperty(value = "当前用户是否收藏该文章")
    private Boolean isCollection;
    @ApiModelProperty(value = "当前用户是否点赞该文章")
    private Boolean isLike;
    @ApiModelProperty(value = "文章标题")
    private String title;
    @ApiModelProperty(value = "频道ID")
    private String channelId;
    @ApiModelProperty(value = "文章内容")
    private String content;
    @ApiModelProperty(value = "文章图片")
    private List<String> photos;
    @ApiModelProperty(value = "创建时间")
//    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "评论数量")
    private String commentCount;}
