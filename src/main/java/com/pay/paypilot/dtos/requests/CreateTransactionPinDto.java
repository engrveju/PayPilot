package com.pay.paypilot.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Setter
public class CreateTransactionPinDto {
    @NotBlank
    @NonNull
    private String oldPin;
    private String newPin;
    private String confirmNewPin;
}
