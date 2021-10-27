package com.program.oss.controller;

import com.program.commonutils.R;
import com.program.commonutils.vo.SaltVo;
import com.program.commonutils.vo.UrlsVo;
import com.program.oss.client.ArticleClient;
import com.program.oss.client.UserClient;
import com.program.commonutils.vo.UrlAndId;
import com.program.oss.service.OssService;
import com.program.oss.service.PictureArticleService;
import com.program.security.security.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ProgramPark/oss")
//@CrossOrigin
public class OssController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ArticleClient articleClient;

    @Autowired
    private OssService ossService;

    @Autowired
    private PictureArticleService pictureArticleService;

    //上传照片的方法
    @PostMapping(value = "/{aid}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadOssFile(@PathVariable("aid") String aid
            , MultipartFile files)
            throws IOException{
        //获取上传文件  MultipartFile
        //返回上传到oss的路径
//        List<String> list=new ArrayList<>();
//        if(files.length > 0){
//            for (MultipartFile photo : files) {
//                if(!photo.isEmpty()){
                    String url = ossService.uploadFileAvatar(files);
//                    list.add(url);
//                }
//            }
            List<String> list=new ArrayList<>();
            list.add(url);
            pictureArticleService.saveUrls(list,aid);
//        }
    }
    //上传头像的方法
    @PostMapping(value = "/uploadSalt")
    public R uploadSalt(MultipartFile files, HttpServletRequest request)
            throws IOException{
//        String memberId = "1";
        String memberId = tokenManager.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        String url = ossService.uploadFileAvatar(files);
        SaltVo saltVo=new SaltVo();
        saltVo.setId(memberId);
        saltVo.setUrl(url);
        userClient.updateSaltById(saltVo);
        UrlAndId urlAndId=new UrlAndId();
        urlAndId.setId(memberId);
        urlAndId.setSalt(url);
        articleClient.getAtcIds1(urlAndId);
        return R.ok();
    }

    @PostMapping("getUrlsById/{id}")
    public UrlsVo getUrlsById(@PathVariable("id") String id){
        UrlsVo urlsVo=new UrlsVo();
        urlsVo.setList(pictureArticleService.getUrls(id));
        return urlsVo;
    }

}
