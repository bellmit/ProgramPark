package com.program.user.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@ApiModel(value="UserInfoVo对象", description="")
public class UserInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户昵称")
    private String username;
    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户邮箱")
    private String email;


}
