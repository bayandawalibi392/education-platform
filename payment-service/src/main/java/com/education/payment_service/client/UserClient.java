package com.education.payment_service.client;

import com.education.payment_service.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/by-username/{username}")
    UserDTO getUserByUsername(@PathVariable String username);

    @PutMapping("/users/update-wallet")
    void updateWallet(@RequestParam String username, @RequestParam Double newBalance);
}
