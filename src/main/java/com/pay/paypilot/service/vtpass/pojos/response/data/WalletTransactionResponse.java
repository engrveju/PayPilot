package com.pay.paypilot.service.vtpass.pojos.response.data;


import com.pay.paypilot.dtos.requests.WalletTransactionDto;
import com.pay.paypilot.restartifacts.BaseResponse;
import java.util.List;

public class WalletTransactionResponse extends BaseResponse {
    private List<WalletTransactionDto> walletTransaction;
}
