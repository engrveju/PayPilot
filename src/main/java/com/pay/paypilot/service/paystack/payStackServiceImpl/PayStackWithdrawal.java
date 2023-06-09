package com.pay.paypilot.service.paystack.payStackServiceImpl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pay.paypilot.dtos.requests.EmailSenderDto;
import com.pay.paypilot.dtos.requests.WithdrawalDto;
import com.pay.paypilot.enums.ResponseCodeEnum;
import com.pay.paypilot.model.BankDetails;
import com.pay.paypilot.model.Transaction;
import com.pay.paypilot.model.User;
import com.pay.paypilot.model.Wallet;
import com.pay.paypilot.repository.BankDetailsRepository;
import com.pay.paypilot.repository.TransactionRepository;
import com.pay.paypilot.repository.UserRepository;
import com.pay.paypilot.repository.WalletRepository;
import com.pay.paypilot.service.EmailService;
import com.pay.paypilot.service.paystack.PayStackWithdrawalService;
import com.pay.paypilot.service.paystack.payStackPojos.*;
import com.pay.paypilot.utils.AppUtil;
import com.pay.paypilot.utils.PayStackUtil;
import com.pay.paypilot.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.pay.paypilot.enums.TransactionStatus.SUCCESS;
import static com.pay.paypilot.enums.TransactionType.DEBIT;
import static com.pay.paypilot.utils.PayStackUtil.*;

@RequiredArgsConstructor
@Service
public class PayStackWithdrawal implements PayStackWithdrawalService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final TransactionRepository walletTransactionRepository;
    private final EmailService emailService;
    private final UserUtil userUtil;
    private final AppUtil appUtil;
    private final PasswordEncoder passwordEncoder;


    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SECRET_KEY);
        return headers;
    }

    private User getUserObjectWithEmail() {
        final String email = userUtil.getAuthenticatedUserEmail();
        final Optional<User> dbUser = userRepository.findByEmail(email);
        return dbUser.isPresent() ? dbUser.get() : null;
    }

    @Override
    public ResponseEntity<List<Bank>> getAllBanks() {
        RestTemplate restTemplate = new RestTemplate();
        BankDetailsResponse response = restTemplate.getForObject(GET_ALL_BANKS, BankDetailsResponse.class);
        if(response != null){
            List<BankResponseDto> banks = response.getData();
            List<Bank> bankList = new ArrayList<>();

            for (BankResponseDto bank : banks) {
                bankList.add(new Bank(bank.getName(), bank.getCode()));
            }
            return ResponseEntity.ok(bankList);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Override
    public ResponseEntity<?> withDrawFromWallet(WithdrawalDto withdrawalDto) {
        final User users = getUserObjectWithEmail();
        System.out.println(withdrawalDto+" sendmoney sendmoney");
        if(users == null)
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);

        Wallet wallet = walletRepository.findByUser_UserId(users.getUserId());

        if (!passwordEncoder.matches(withdrawalDto.getWalletPin(), wallet.getPin()))
            return new ResponseEntity<>("Invalid transaction pin", HttpStatus.UNAUTHORIZED);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> allBankResponse;

        //QUERY TO FETCH FULL BANK DETAILS USING ACCOUNT NUMBER AND CORRESPONDING BANK CODE
        try {
            allBankResponse = restTemplate.exchange(RESOLVE_BANK
                            + "?account_number=" + withdrawalDto.getAccountNumber()
                            + "&bank_code=" + withdrawalDto.getBankCode(),
                    HttpMethod.GET, new HttpEntity<>(getHeaders()), String.class);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Incorrect bank details", HttpStatus.BAD_REQUEST);
        }

        if (!allBankResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>("An error occurred", HttpStatus.BAD_REQUEST);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            WithdrawalResponse verifiedBankDetails = mapper.readValue(allBankResponse.getBody(),
                    WithdrawalResponse.class);

            if (verifyWithdrawalAmount(withdrawalDto.getAmount(), wallet) > 0) {
                BankDetails bankDetails = bankDetailsRepository.findByAccountNumberAndBankCode(
                        withdrawalDto.getAccountNumber(), withdrawalDto.getBankCode());

                //IF BANK DETAILS IS NOT SAVED, SAVE IT AND PROCEED WITH TRANSACTION
                if (Objects.isNull(bankDetails)) {
                    bankDetails = new BankDetails();

                    bankDetails.setWallet(walletRepository.findByUser_UserId(users.getUserId()));
                    bankDetails.setAccountName(verifiedBankDetails.getData().getAccountName());
                    bankDetails.setAccountNumber(verifiedBankDetails.getData().getAccountNumber());
                    bankDetails.setBankCode(withdrawalDto.getBankCode());
                    bankDetails.setBankId(verifiedBankDetails.getData().getBankId());

                    bankDetailsRepository.save(bankDetails);
                    wallet.getBankDetails().add(bankDetails);
                }

                TransferRecipient transferRecipient = TransferRecipient.builder()
                        .name(bankDetails.getAccountName())
                        .accountNumber(bankDetails.getAccountNumber())
                        .bankCode(bankDetails.getBankCode())
                        .email(users.getEmail())
                        .amount(withdrawalDto.getAmount())
                        .build();

                return createTransferRecipient(transferRecipient, bankDetails, wallet);
            } else {
                    return new ResponseEntity<>("Insufficient Balance in your wallet", HttpStatus.BAD_REQUEST);
            }

        } catch(IOException e){
            e.printStackTrace();
            return new ResponseEntity<>("Check your bank details", HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public ResponseEntity<String> verifyAccountNumber(String accountNumber, String bankCode) {
        ResponseEntity<WithdrawalResponse> response = new RestTemplate().exchange(RESOLVE_BANK +
                        "?account_number=" + accountNumber + "&bank_code=" + bankCode,
                HttpMethod.GET, new HttpEntity<>(getHeaders()), WithdrawalResponse.class);

        WithdrawalResponse withdrawalResponse = response.getBody();
        if (withdrawalResponse == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        String accountName = withdrawalResponse.getData().getAccountName();

        if (response.getStatusCode().is2xxSuccessful()){
            return new ResponseEntity<>(accountName, HttpStatus.ACCEPTED);
        }
        return  new ResponseEntity<>("Account number not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<SendMoneyResponse> createTransferRecipient(TransferRecipient transferRecipient,
                                                                     BankDetails bankDetails, Wallet wallet) {
        HttpEntity<TransferRecipient> request = new HttpEntity<>(transferRecipient, getHeaders());

        ResponseEntity<WithdrawalResponse> response = new RestTemplate().postForEntity(
                CREATE_TRANSFER_RECEIPIENT
                , request, WithdrawalResponse.class
        );

        WithdrawalResponse transferRecipientResponse = response.getBody();
        if (transferRecipientResponse == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        String recipient = transferRecipientResponse.getData().getRecipientCode();

        if (response.getStatusCode().is2xxSuccessful()) {
            TransferRequest transferRequest = TransferRequest.builder()
                    .recipient(recipient)
                    .amount(transferRecipient.getAmount())
                    .reference(generateTransactionReference())
                    .build();
            return initiateTransfer(transferRequest, bankDetails, wallet);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<SendMoneyResponse> initiateTransfer(TransferRequest transferRequest,
                                                              BankDetails bankDetails, Wallet wallet){
        HttpEntity<TransferRequest> request = new HttpEntity<>(transferRequest, getHeaders());

        ResponseEntity<String> response = new RestTemplate().postForEntity(INITIATE_TRANSFER, request,
                String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            updateWallet(transferRequest, wallet, bankDetails);
            SendMoneyResponse sendMoneyResponse = SendMoneyResponse.builder()
                    .RecipientName(bankDetails.getAccountName())
                    .recipientBankCode(bankDetails.getBankCode())
                    .recipientAccountNumber(appUtil.concealAccountNumber(bankDetails.getAccountNumber()))
                    .transactionReference(transferRequest.getReference())
                    .amountSent(transferRequest.getAmount())
                    .date(new Date()).build();
            sendMoneyResponse.setCode(0);
            sendMoneyResponse.setDescription(ResponseCodeEnum.SUCCESS.getDescription());
            return new ResponseEntity<>(sendMoneyResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //CHECKS IF AMOUNT TO BE WITHDRAWN IS LESS OR EQUAL TO WALLET BALANCE

    public int verifyWithdrawalAmount(BigDecimal amount, Wallet wallet){
        if(wallet.getAccountBalance().compareTo(amount)>=0)
            return 1;
        return -1;
    }

    public void updateWallet(TransferRequest transferRequest, Wallet wallet, BankDetails bankDetails){
        BigDecimal balance = wallet.getAccountBalance().subtract(transferRequest.getAmount());
        wallet.setAccountBalance(balance);
        walletRepository.save(wallet);

        Transaction walletTransaction = Transaction.builder()
                .name(bankDetails.getAccountName())
                .wallet(wallet)
                .transactionType(DEBIT)
                .amount(transferRequest.getAmount())
                .transactionReference(transferRequest.getReference())
                .transactionStatus(SUCCESS)
                .build();
        walletTransactionRepository.save(walletTransaction);

        EmailSenderDto mailDto = EmailSenderDto.builder()
                .to(wallet.getUser().getEmail())
                .subject("Debit Alert: "+ "N "+ transferRequest.getAmount())
                .content("A withdrawal of "+ transferRequest.getAmount()+" has been sent to : "
                        +bankDetails.getAccountName()+" " +appUtil.concealAccountNumber(bankDetails.getAccountNumber())
                        + " Your new balance is now: "
                        + wallet.getAccountBalance() +" Reference: "+ transferRequest.getReference())
                .build();
        emailService.sendMail(mailDto);
    }
}
