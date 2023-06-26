package com.pay.paypilot.service.vtpass.pojos.response.electricity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VerifyMerchantResponse {
    private String code;
    private Content content;
}
