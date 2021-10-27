package com.program.atc.client;

import com.program.atc.config.FeignConfig;
import com.program.commonutils.vo.UserFrontVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name="program-user")
public interface UserClient {
    @GetMapping("/ProgramPark/user/getUserById/{id}")
    public UserFrontVo getUserById(@PathVariable("id") String id);

    @PostMapping("/ProgramPark/user/isAttention/{id1}/{id2}")
    public String isAttention(@PathVariable("id1") String id1,@PathVariable("id2") String id2);
}
