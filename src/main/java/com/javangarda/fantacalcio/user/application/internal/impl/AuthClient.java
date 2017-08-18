package com.javangarda.fantacalcio.user.application.internal.impl;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("fantacalcio-auth-server")
public interface AuthClient {

    @GetMapping(value = "/hell")
    Integer hell();
}
