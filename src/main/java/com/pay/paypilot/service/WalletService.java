package com.pay.paypilot.service;


import com.pay.paypilot.dtos.requests.CreateTransactionPinDto;
import com.pay.paypilot.dtos.requests.WithdrawalDto;
import com.pay.paypilot.dtos.response.WalletResponse;
import com.pay.paypilot.restartifacts.BaseResponse;
import com.pay.paypilot.service.paystack.payStackPojos.Bank;
import com.pay.paypilot.service.vtpass.pojos.request.BuyAirtimeRequest;
import com.pay.paypilot.service.vtpass.pojos.request.BuyDataPlanRequest;
import com.pay.paypilot.service.vtpass.pojos.request.BuyElectricityRequest;
import com.pay.paypilot.service.vtpass.pojos.request.VerifyMerchantRequest;
import com.pay.paypilot.service.vtpass.pojos.response.data.*;
import com.pay.paypilot.service.vtpass.pojos.response.electricity.BuyElectricityResponse;
import com.pay.paypilot.service.vtpass.pojos.response.electricity.VerifyMerchantResponse;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    WalletResponse getWalletBalance();
    ResponseEntity<String> fundWallet(BigDecimal bigDecimal, String transactionType);
    ResponseEntity<String> verifyPayment(String reference, String transactionType);
    BaseResponse updateWalletPin(CreateTransactionPinDto createTransactionPinDto);
    ResponseEntity<List<Bank>> getAllBanks();
    ResponseEntity<?> walletWithdrawal(WithdrawalDto withdrawalDto);
    ResponseEntity<String> verifyAccountNumber(String accountNumber, String bankCode);

    DataServicesResponse getDataServices();
    DataPlansResponse getDataPlans(String dataType);
    BuyDataPlanResponse buyDataPlan(BuyDataPlanRequest request, String pin);
    BuyAirtimeResponse buyAirtimeServices(BuyAirtimeRequest buyAirtimeRequest , String pin);

    AirtimeServiceResponse getAirtimeServices();

    DataServicesResponse getAllElectricityService();

    VerifyMerchantResponse verifyElectricityMeter(VerifyMerchantRequest merchantRequest);

    BuyElectricityResponse buyElectricity(BuyElectricityRequest electricityRequest, String pin);



}
