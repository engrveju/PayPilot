package com.pay.paypilot.service;


import com.pay.paypilot.service.vtpass.pojos.response.data.TransactionResponseViewModel;

public interface TransactionService {
    TransactionResponseViewModel viewWalletTransaction(int page, int limit);
}
