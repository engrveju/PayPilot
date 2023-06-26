package com.pay.paypilot.controller;

import com.pay.paypilot.dtos.requests.CreateTransactionPinDto;
import com.pay.paypilot.dtos.requests.WithdrawalDto;
import com.pay.paypilot.dtos.response.WalletResponse;
import com.pay.paypilot.restartifacts.BaseResponse;
import com.pay.paypilot.service.WalletService;
import com.pay.paypilot.service.paystack.payStackPojos.Bank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/wallet")
public class WalletController {
    private final WalletService walletService;
    @PutMapping("/updateWalletPin")
    public BaseResponse updateWalletPin(@RequestBody CreateTransactionPinDto createTransactionPinDto){
        return walletService.updateWalletPin(createTransactionPinDto);
    }
    @GetMapping("/balance")
    public ResponseEntity<WalletResponse> getBalance() {
        WalletResponse response = walletService.getWalletBalance();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/fundWallet")
    public ResponseEntity<String> fundWallet(@RequestParam BigDecimal amount){
        return walletService.fundWallet(amount, "fundwallet");
    }

    @GetMapping("/verifyPayment/{reference}/{paymentMethod}")
    public ResponseEntity<String> verifyPayment(@PathVariable String reference, @PathVariable  String paymentMethod){
        return walletService.verifyPayment(reference,paymentMethod);
    }
    @PostMapping("/getBankDetails")
    public ResponseEntity<List<Bank>> fetchBankDetails(){
        return walletService.getAllBanks();
    }
    @PostMapping("/sendMoney")
    public ResponseEntity<?> walletWithdrawal(@RequestBody WithdrawalDto withdrawalDto){
        return walletService.walletWithdrawal(withdrawalDto);
    }
    @PostMapping("/verifyAccountNumber")
    public ResponseEntity<String> verifyAccountNumber(@RequestParam String accountNumber, @RequestParam String bankCode){
        return walletService.verifyAccountNumber(accountNumber, bankCode);
    }
}
