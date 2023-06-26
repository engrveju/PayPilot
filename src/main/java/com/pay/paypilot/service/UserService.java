package com.pay.paypilot.service;

import com.pay.paypilot.dtos.requests.*;
import com.pay.paypilot.dtos.response.SocialLoginResponse;
import com.pay.paypilot.restartifacts.BaseResponse;

public interface UserService {
    BaseResponse signUp(CreateUserRequest createUserRequest);

    BaseResponse confirmRegistration(String token);

    BaseResponse login(LoginUserRequest request);

    BaseResponse forgotPasswordRequest(ForgetPasswordRequest forgotPasswordRequest);

    BaseResponse resetPassword(ResetPasswordRequest request, String token);

    SocialLoginResponse socialLogin(SocialLoginUserRequest request);
}
