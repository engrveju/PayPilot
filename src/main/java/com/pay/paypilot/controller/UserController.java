package com.pay.paypilot.controller;


import com.pay.paypilot.dtos.requests.CreateUserRequest;
import com.pay.paypilot.dtos.requests.ForgetPasswordRequest;
import com.pay.paypilot.dtos.requests.ResetPasswordRequest;
import com.pay.paypilot.restartifacts.BaseResponse;
import com.pay.paypilot.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public BaseResponse createUser(@RequestBody CreateUserRequest createUserRequest){
        return userService.signUp(createUserRequest);
    }
    @GetMapping("/confirmRegistration")
    public BaseResponse confirmRegistration(@RequestParam (name = "token") String token) {
        return userService.confirmRegistration(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponse> forgotPassword(@RequestBody @Valid ForgetPasswordRequest request){
        return new ResponseEntity<>(userService.forgotPasswordRequest(request), HttpStatus.OK);
    }

    @PostMapping("/reset-password/{token}")
    public BaseResponse resetPassword(@RequestBody @Valid ResetPasswordRequest request, @PathVariable String token){
        return userService.resetPassword(request, token);
    }

}
