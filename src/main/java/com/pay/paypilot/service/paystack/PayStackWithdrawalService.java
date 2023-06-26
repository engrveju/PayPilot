package com.pay.paypilot.service.paystack;

import com.pay.paypilot.dtos.requests.WithdrawalDto;
import com.pay.paypilot.service.paystack.payStackPojos.Bank;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PayStackWithdrawalService {
    ResponseEntity<List<Bank>> getAllBanks();
    ResponseEntity<?> withDrawFromWallet(WithdrawalDto withdrawalDto);
    ResponseEntity<String> verifyAccountNumber(String account_number, String bank_code);
}
