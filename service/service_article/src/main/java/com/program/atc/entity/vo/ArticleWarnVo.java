package com.program.atc.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleWarnVo {
    @ApiModelProperty(value = "文章标题")
    private String title;
}
