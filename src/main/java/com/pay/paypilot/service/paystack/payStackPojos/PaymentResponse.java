package com.pay.paypilot.service.paystack.payStackPojos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PaymentResponse {
    private boolean status;
    private String message;
    private PaymentDto data;
}
