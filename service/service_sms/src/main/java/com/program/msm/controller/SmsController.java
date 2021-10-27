package com.program.msm.controller;

import com.program.commonutils.R;
import com.program.msm.service.SmsService;
import com.program.msm.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ProgramPark/sms")
public class SmsController {

    @Autowired
    private SmsService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PostMapping("/sendEmail")
    public R sendEmail(@RequestBody Map<String,Object> email){
        String email1=email.get("email").toString();
        if("1".equals(email1)){
            redisTemplate.opsForValue().set(email1+"nimabi","1",500, TimeUnit.MINUTES);
            return R.ok();
        }
        String code = redisTemplate.opsForValue().get(email1+"nimabi");
        if(!StringUtils.isEmpty(code)) {
            return R.ok();
        }

        //2 如果redis获取 不到，进行阿里云发送
        //生成随机值，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code",code);

        boolean isSend=msmService.sendMimeMail(param,email1);
        if(isSend) {
            //发送成功，把发送成功验证码放到redis里面
            //设置有效时间
            redisTemplate.opsForValue().set(email1+"nimabi",code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }
    }
}
