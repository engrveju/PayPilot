package com.pay.paypilot.service.paystack.payStackPojos;


import com.pay.paypilot.restartifacts.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class SendMoneyResponse extends BaseResponse {
    private String RecipientName;
    private String recipientBankCode;
    private String recipientAccountNumber;
    private BigDecimal amountSent;
    private String transactionReference;
    private Date date;

}
