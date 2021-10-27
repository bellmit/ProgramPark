package com.program.atc.entity.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("atc_comment")
@ApiModel(value="Comment对象", description="")
public class CommentFrontVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "被评论者ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "评论者ID")
    private String userId;

    @ApiModelProperty(value = "文章ID")
    private String articleId;

    @ApiModelProperty(value = "用户昵称")
    private String username;

    @ApiModelProperty(value = "自己昵称")
    private String name;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "作者ID")
    private String authorId;

    @ApiModelProperty(value = "@的评论ID")
    private String id2;

    @ApiModelProperty(value = "父ID")
    private String parentId;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;
}