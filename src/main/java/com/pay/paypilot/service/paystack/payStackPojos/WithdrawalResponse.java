package com.pay.paypilot.service.paystack.payStackPojos;


import lombok.Setter;

@Setter
@lombok.Data
public class WithdrawalResponse {
    private boolean status;
    private String message;
    private PaystackWithdrawalDto data;
}