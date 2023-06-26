package com.pay.paypilot.service.vtpass.pojos.response.data;

import com.decagon.dev.paybuddy.dtos.requests.WalletTransactionDto;
import com.decagon.dev.paybuddy.restartifacts.BaseResponse;

import java.util.List;

public class WalletTransactionResponse extends BaseResponse {
    private List<WalletTransactionDto> walletTransaction;
}
