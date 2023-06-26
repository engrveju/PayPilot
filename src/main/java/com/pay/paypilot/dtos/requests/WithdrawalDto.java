package com.pay.paypilot.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class WithdrawalDto {
    @NotBlank
    private String accountNumber;
    @NotBlank
    private String bankCode;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String walletPin;
}
