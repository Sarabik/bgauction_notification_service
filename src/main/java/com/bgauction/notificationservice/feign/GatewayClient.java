package com.bgauction.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "GATEWAY", configuration = ClientFeignConfig.class)
public interface GatewayClient {

    @GetMapping("/internal/game/{id}")
    ResponseEntity<Game> getGameById(@PathVariable Long id);

    @GetMapping("/internal/user/{id}")
    ResponseEntity<User> getUserById(@PathVariable Long id);

}
