package com.program.oss.client;

import com.program.commonutils.vo.SaltVo;
import com.program.commonutils.vo.UserFrontVo;
import com.program.oss.config.FeignConfig2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name="program-user")
public interface UserClient {
    @GetMapping("/ProgramPark/user/getUserById/{id}")
    public UserFrontVo getUserById(@PathVariable("id") String id);
    @PostMapping("/ProgramPark/user/updateSaltById")
    public void updateSaltById(@RequestBody SaltVo saltVo);
}
