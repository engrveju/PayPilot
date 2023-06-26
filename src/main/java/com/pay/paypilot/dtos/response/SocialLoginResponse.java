package com.pay.paypilot.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SocialLoginResponse {
    private String jwtToken;
    private int loginCount;
}
