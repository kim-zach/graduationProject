package com.kimi.kel.sms.client;

import com.kimi.kel.sms.client.fallback.CoreUserInfoClientFallback;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(value = "service-core",fallback = CoreUserInfoClientFallback.class)
public interface CoreUserInfoClient {


    @ApiOperation(value = "校验手机号是否注册")
    @GetMapping("/api/core/userInfo/checkMobile/{mobile}")  //访问地址要写全
    boolean checkMobile(@PathVariable String mobile);
}
