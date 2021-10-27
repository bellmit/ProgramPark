package com.program.msm.service.impl;

import com.program.msm.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private JavaMailSender mailSender;//一定要用@Autowired

    //application.properties中已配置的值
    @Value("${spring.mail.username}")
    private String from;

    //发送短信的方法
//    @Override
//    public boolean send(Map<String, Object> param, String phone) {
//        if(StringUtils.isEmpty(phone)) return false;
//
//        DefaultProfile profile =
//                DefaultProfile.getProfile("default", "LTAI4Fy76hybzhPUECfCt19E", "jJZ2Nk9JXt8YH94UG3ZIApy7ltRuXe");
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        //设置相关固定的参数
//        CommonRequest request = new CommonRequest();
//        //request.setProtocol(ProtocolType.HTTPS);
//        request.setMethod(MethodType.POST);
//        request.setDomain("dysmsapi.aliyuncs.com");
//        request.setVersion("2017-05-25");
//        request.setAction("SendSms");
//
//        //设置发送相关的参数
//        request.putQueryParameter("PhoneNumbers",phone); //手机号
//        request.putQueryParameter("SignName","我的谷粒在线教育网站"); //申请阿里云 签名名称
//        request.putQueryParameter("TemplateCode","SMS_213772127"); //申请阿里云 模板code
//        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); //验证码数据，转换json数据传递
//
//        try {
//            //最终发送
//            CommonResponse response = client.getCommonResponse(request);
//            boolean success = response.getHttpResponse().isSuccess();
//            return success;
//        }catch(Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }

    /**
     * 给前端输入的邮箱，发送验证码
     * @param email
     * @return
     */
    public boolean sendMimeMail(Map<String, Object> param, String email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("验证码邮件");//主题
            //生成随机数
            String code = (String) param.get("code");

            mailMessage.setText("您收到的验证码是："+code);//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送
            return  true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 随机生成6位数的验证码
     * @return String code
     */
    public String randomCode(){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

}
