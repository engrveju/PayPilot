package com.pay.paypilot.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginUserRequest {
    @Email(message = "Provide an valid email address")
    private String email;
    @NotBlank(message = "Confirm password name cannot be blank")
    private  String password;
}
