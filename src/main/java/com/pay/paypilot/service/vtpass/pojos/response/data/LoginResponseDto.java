package com.pay.paypilot.service.vtpass.pojos.response.data;

import com.pay.paypilot.restartifacts.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginResponseDto extends BaseResponse {
    private String firstName;
    private String lastName;
    private String email;
    private int loginCount;
    private String token;
}
