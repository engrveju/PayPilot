package com.pay.paypilot.controller;

import com.pay.paypilot.dtos.requests.LoginUserRequest;
import com.pay.paypilot.dtos.requests.SocialLoginUserRequest;
import com.pay.paypilot.dtos.response.SocialLoginResponse;
import com.pay.paypilot.restartifacts.BaseResponse;
import com.pay.paypilot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginUserRequest request) {
        return userService.login(request);
    }
    @PostMapping("/social-login")
    public SocialLoginResponse socialLogin(@RequestBody SocialLoginUserRequest request){
        return userService.socialLogin(request);
    }
}
