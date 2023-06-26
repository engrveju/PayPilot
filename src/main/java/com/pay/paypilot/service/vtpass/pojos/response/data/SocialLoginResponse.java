package com.pay.paypilot.service.vtpass.pojos.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SocialLoginResponse {
    private String jwtToken;
    private int loginCount;
}
