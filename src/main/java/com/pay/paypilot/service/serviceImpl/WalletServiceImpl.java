package com.pay.paypilot.service.serviceImpl;

import com.pay.paypilot.dtos.requests.CreateTransactionPinDto;
import com.pay.paypilot.dtos.requests.WithdrawalDto;
import com.pay.paypilot.dtos.response.WalletResponse;
import com.pay.paypilot.enums.ResponseCodeEnum;
import com.pay.paypilot.enums.TransactionStatus;
import com.pay.paypilot.enums.TransactionType;
import com.pay.paypilot.exceptions.IncorrectMerchantIdentity;
import com.pay.paypilot.exceptions.WalletServiceException;
import com.pay.paypilot.model.Transaction;
import com.pay.paypilot.model.User;
import com.pay.paypilot.model.Wallet;
import com.pay.paypilot.repository.TransactionRepository;
import com.pay.paypilot.repository.UserRepository;
import com.pay.paypilot.repository.WalletRepository;
import com.pay.paypilot.restartifacts.BaseResponse;
import com.pay.paypilot.service.WalletService;
import com.pay.paypilot.service.paystack.PayStackWithdrawalService;
import com.pay.paypilot.service.paystack.PaystackPaymentService;
import com.pay.paypilot.service.paystack.payStackPojos.Bank;
import com.pay.paypilot.service.vtpass.VTPassService;
import com.pay.paypilot.service.vtpass.pojos.request.BuyAirtimeRequest;
import com.pay.paypilot.service.vtpass.pojos.request.BuyDataPlanRequest;
import com.pay.paypilot.service.vtpass.pojos.request.BuyElectricityRequest;
import com.pay.paypilot.service.vtpass.pojos.request.VerifyMerchantRequest;
import com.pay.paypilot.service.vtpass.pojos.response.data.*;
import com.pay.paypilot.service.vtpass.pojos.response.electricity.BuyElectricityResponse;
import com.pay.paypilot.service.vtpass.pojos.response.electricity.VerifyMerchantResponse;
import com.pay.paypilot.utils.ResponseCodeUtil;
import com.pay.paypilot.utils.UserUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final PayStackWithdrawalService payStackWithdrawalService;
    private final PaystackPaymentService paystackPaymentService;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final UserUtil userUtil;
    private final VTPassService vtPassService;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;
    private final ResponseCodeUtil responseCodeUtil = new ResponseCodeUtil();
    public User getLoggedInUser() {
        String authentication = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(authentication).orElseThrow(() -> new RuntimeException("User not authorized"));
    }
    @Override
    public WalletResponse getWalletBalance() {
        WalletResponse walletResponse;
        try {
            User walletOwner = getLoggedInUser();
            Wallet wallet = walletRepository.findWalletByUser_Email(walletOwner.getEmail());
            walletResponse = WalletResponse.builder()
                    .userName(walletOwner.getFirstName() + " " + walletOwner.getLastName())
                    .walletBalance(wallet.getAccountBalance())
                    .accountNumber(wallet.getAccountNumber())
                    .isPinUpdated(wallet.isPinUpdated())
                    .build();
            return responseCodeUtil.updateResponseData(walletResponse, ResponseCodeEnum.SUCCESS, "Wallet Balance");
        } catch (Exception e) {
            log.error("Email not registered, Wallet balance cannot be displayed: {}", e.getMessage());
            walletResponse = WalletResponse.builder()
                    .walletBalance(null)
                    .build();
            return responseCodeUtil.updateResponseData(walletResponse, ResponseCodeEnum.ERROR);
        }
    }

    @Override
    public ResponseEntity<String> fundWallet(BigDecimal amount, String transactionType) {
        return paystackPaymentService.paystackPayment(amount,transactionType);
    }

    @Override
    public ResponseEntity<String> verifyPayment(String reference, String transactionType) {
        return paystackPaymentService.verifyPayment(reference, transactionType);
    }


    public BaseResponse updateWalletPin (CreateTransactionPinDto createTransactionPinDto) {
        BaseResponse baseResponse = new BaseResponse();
        String authEmail = userUtil.getAuthenticatedUserEmail();

        Wallet userWallet = walletRepository.findWalletByUser_Email(authEmail);
        if (userWallet != null){
            log.info("Database pin: {}", passwordEncoder.encode("0000"));
            log.info("Database pin: {}", userWallet.getPin());
            log.info("User pin: {}", createTransactionPinDto.getOldPin());

            if(passwordEncoder.matches(createTransactionPinDto.getOldPin(), userWallet.getPin())){
                if(createTransactionPinDto.getNewPin().equals(createTransactionPinDto.getConfirmNewPin())){
                    userWallet.setPin(passwordEncoder.encode(createTransactionPinDto.getNewPin()));
                    userWallet.setPinUpdated(true);
                    walletRepository.save(userWallet);
                    return responseCodeUtil.updateResponseData(baseResponse, ResponseCodeEnum.SUCCESS,
                            "Wallet pin successfully changed");
                } else{
                    return responseCodeUtil.updateResponseData(baseResponse, ResponseCodeEnum.ERROR,
                            "New pin does not match the confirmed pin");
                }
            }else {
                return responseCodeUtil.updateResponseData(baseResponse, ResponseCodeEnum.ERROR,
                        "Old pin does not match existing pin");
            }
        }
        return responseCodeUtil.updateResponseData(baseResponse, ResponseCodeEnum.ERROR,
                "Wallet not found");
    }
    @Override
    public ResponseEntity<List<Bank>> getAllBanks() {

        return payStackWithdrawalService.getAllBanks();
    }

    @Override
    public ResponseEntity<?> walletWithdrawal(WithdrawalDto withdrawalDto) {
        return payStackWithdrawalService.withDrawFromWallet(withdrawalDto);
    }

    @Override
    public ResponseEntity<String> verifyAccountNumber(String accountNumber, String bankCode) {
        return payStackWithdrawalService.verifyAccountNumber(accountNumber, bankCode);
    }


    @Override
    public DataServicesResponse getDataServices() {
        return vtPassService.getDataServices();
    }

    @Override
    public DataPlansResponse getDataPlans(String dataType) {
        return vtPassService.getDataPlans(dataType);
    }

    @Override
    public BuyDataPlanResponse buyDataPlan(BuyDataPlanRequest request, String pin) {
        Wallet wallet = walletRepository.findWalletByUser_Email(getLoggedInUser().getEmail());

        if (!passwordEncoder.matches(pin, wallet.getPin()))  //Check pin accuracy
            throw new WalletServiceException("Pin is wrong");

        if (wallet.getAccountBalance().compareTo(request.getAmount()) <= 0) //Check if wallet balance can perform transaction
            throw new WalletServiceException("Insufficient balance");

        BuyDataPlanResponse response = vtPassService.payDataPlan(request); //Buy data
        if (Objects.equals(response.response_description, "TRANSACTION SUCCESSFUL")) {//UpdateWallet and save Transaction
            wallet.setAccountBalance(wallet.getAccountBalance().subtract(request.getAmount())); //Deduct the wallet
            Wallet updatedWallet = walletRepository.save(wallet);

            Transaction walletTransaction = Transaction.builder()
                    .name(request.getServiceID())
                    .wallet(updatedWallet)
                    .transactionType(TransactionType.DEBIT)
                    .amount(request.getAmount())
                    .transactionReference(response.requestId)
                    .transactionStatus(TransactionStatus.SUCCESS)
                    .build();
            transactionRepository.save(walletTransaction);
        }

        return response;
    }

    @Override
    public DataServicesResponse getAllElectricityService()
    {

        return vtPassService.getAllElectricityService();
    }

    @Override
    public VerifyMerchantResponse verifyElectricityMeter(VerifyMerchantRequest merchantRequest)
    {
        return vtPassService.verifyElectricityMeter(merchantRequest);
    }

    @Override
    @Transactional
    public BuyElectricityResponse buyElectricity(BuyElectricityRequest electricityRequest, String pin)
    {



        User walletOwner = getLoggedInUser();

        Wallet wallet = walletRepository.findWalletByUser_Email(walletOwner.getEmail());


        if (!passwordEncoder.matches(pin, wallet.getPin()))
            throw new WalletServiceException("Pin is wrong");

        if (wallet.getAccountBalance().compareTo(electricityRequest.getAmount()) <= 0) //Check if wallet balance can perform transaction
            throw new WalletServiceException("Insufficient balance");


        VerifyMerchantRequest merchantRequest = new VerifyMerchantRequest();
        merchantRequest.setServiceID(electricityRequest.getServiceID());
        merchantRequest.setBillersCode(electricityRequest.getBillersCode());
        merchantRequest.setType(electricityRequest.getVariation_code());
        VerifyMerchantResponse verifyMerchantResponse =  verifyElectricityMeter(merchantRequest);


        if(!verifyMerchantResponse.getCode().equals("000"))
        {
            throw new IncorrectMerchantIdentity("incorrect meter number");
        }

        BuyElectricityResponse response = vtPassService.buyElectricity(electricityRequest);
        if (Objects.equals(response.getResponse_description(), "TRANSACTION SUCCESSFUL"))
        {
            wallet.setAccountBalance(wallet.getAccountBalance().subtract(electricityRequest.getAmount())); //Deduct the wallet

            Transaction walletTransaction = Transaction.builder()
                    .name(electricityRequest.getServiceID())
                    .wallet(wallet)
                    .transactionType(TransactionType.DEBIT)
                    .amount(electricityRequest.getAmount())
                    .transactionReference(response.getExchangeReference())
                    .transactionStatus(TransactionStatus.SUCCESS)
                    .build();

        }

        return response;
    }
    public BuyAirtimeResponse buyAirtimeServices(BuyAirtimeRequest buyAirtimeRequest , String pin) {
        Wallet wallet = walletRepository.findWalletByUser_Email(getLoggedInUser().getEmail());


        if (!passwordEncoder.matches(pin, wallet.getPin()))  //Check pin accuracy
            throw new WalletServiceException("Pin is wrong");

        if (wallet.getAccountBalance().compareTo(buyAirtimeRequest.getAmount()) <= 0) //Check if wallet balance can perform transaction
            throw new WalletServiceException("Insufficient balance");

        BuyAirtimeResponse buyAirtimeResponse = vtPassService.buyAirtime(buyAirtimeRequest);

        if (this.isSuccessful(buyAirtimeResponse.getResponse_description())) {
            Wallet updatedWallet = deductCharges(wallet, buyAirtimeRequest.getAmount());//Deduct the wallet

            Transaction walletTransaction = Transaction.builder()
                    .name(buyAirtimeRequest.getServiceID())
                    .wallet(updatedWallet)
                    .transactionType(TransactionType.DEBIT)
                    .amount(buyAirtimeRequest.getAmount())
                    .transactionReference(buyAirtimeRequest.getRequest_id())
                    .transactionStatus(TransactionStatus.SUCCESS)
                    .build();
            transactionRepository.save(walletTransaction);
        }
        return buyAirtimeResponse;
    }

    @Override
    public AirtimeServiceResponse getAirtimeServices() {
        return vtPassService.getAirtimeServices() ;
    }

    private Wallet deductCharges(Wallet wallet, BigDecimal amount){
        wallet.setAccountBalance(wallet.getAccountBalance().subtract(amount)); //Deduct the wallet
        Wallet updatedWallet = walletRepository.save(wallet);
        return  updatedWallet;
    }
    private  boolean isSuccessful(String response_description){
        if(response_description.equals("TRANSACTION SUCCESSFUL"))
            return true;
        return false;
    }

}
