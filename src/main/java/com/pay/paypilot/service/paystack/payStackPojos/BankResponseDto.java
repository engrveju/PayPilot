package com.pay.paypilot.service.paystack.payStackPojos;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BankResponseDto {
    private String name;
    private String code;

}
