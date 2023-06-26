package com.pay.paypilot.service.vtpass.pojos.response.data;

import com.pay.paypilot.restartifacts.BaseResponse;
import lombok.*;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class WalletResponse extends BaseResponse {
    private String userName;
    private BigDecimal walletBalance;
    private String accountNumber;
    private boolean isPinUpdated;
}
