package com.movii.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.movii.service.dto.AdminUserDTO;

@FeignClient(name = "movii", configuration = { UserFeignClientInterceptor.class })
public interface UserFeignClient {

    @GetMapping("/api/admin/users/{login}")
    public AdminUserDTO userId(@PathVariable String login);
}
