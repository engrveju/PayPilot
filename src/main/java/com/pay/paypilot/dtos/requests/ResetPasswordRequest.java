package com.pay.paypilot.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequest {
    @NotBlank(message = "New password field can't be empty")
    private String newPassword;
    @NotBlank(message = "Confirm password field can't be empty")
    private String confirmPassword;
}
