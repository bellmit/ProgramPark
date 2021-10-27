package com.program.atc.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data

public class ArticleFrontVo {
        @ApiModelProperty(value = "文章ID")
        private String id;
        @ApiModelProperty(value = "作者名称")
        private String authorName;
        @ApiModelProperty(value = "文章标题")
        private String title;
        @ApiModelProperty(value = "文章封面照片")
        private List<String> photos;
        @ApiModelProperty(value = "创建时间")
        @TableField(fill = FieldFill.INSERT)
        private Date gmtCreate;
        @ApiModelProperty(value = "评论数量")
        private String commentCount;

}
