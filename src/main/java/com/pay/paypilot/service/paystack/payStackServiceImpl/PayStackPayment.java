package com.pay.paypilot.service.paystack.payStackServiceImpl;

import com.pay.paypilot.enums.TransactionStatus;
import com.pay.paypilot.enums.TransactionType;
import com.pay.paypilot.model.Transaction;
import com.pay.paypilot.model.User;
import com.pay.paypilot.model.Wallet;
import com.pay.paypilot.repository.TransactionRepository;
import com.pay.paypilot.repository.UserRepository;
import com.pay.paypilot.repository.WalletRepository;
import com.pay.paypilot.service.paystack.PaystackPaymentService;
import com.pay.paypilot.service.paystack.payStackPojos.PaymentDto;
import com.pay.paypilot.service.paystack.payStackPojos.PaymentResponse;
import com.pay.paypilot.utils.PayStackUtil;
import com.pay.paypilot.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;

import static com.pay.paypilot.enums.TransactionType.FUNDWALLET;
import static com.pay.paypilot.utils.PayStackUtil.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class PayStackPayment implements PaystackPaymentService {
    private Wallet wallet;
    private BigDecimal fundingAmount;
    private String paymentReference;
    private String userEmail;
    private final UserUtil userUtil;
    private final WalletRepository walletRepository;
    private final TransactionRepository walletTransactionRepository;
    private final UserRepository userRepository;

    public ResponseEntity<String> paystackPayment(BigDecimal amount, String transactionType) {
        userEmail = userUtil.getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(userEmail).get();
        wallet = walletRepository.findByUser_UserId(user.getUserId());

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAmount(amount);
        paymentDto.setReference(PayStackUtil.generateTransactionReference());
        paymentDto.setEmail(userEmail);
        paymentReference = paymentDto.getReference();
        fundingAmount = paymentDto.getAmount();
        paymentDto.setAmount(fundingAmount.multiply(BigDecimal.valueOf(100)));
        if(transactionType.equalsIgnoreCase("makepayment")){
            paymentDto.setTransactionType(TransactionType.MAKEPAYMENT.getTransaction());
        }else{
            paymentDto.setTransactionType(FUNDWALLET.getTransaction());
        }
        paymentDto.setCallback_url(PayStackUtil.CALLBACK_URL+paymentDto.getReference()+"/"+paymentDto.getTransactionType());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+ SECRET_KEY);

        HttpEntity<PaymentDto> entity = new HttpEntity<>(paymentDto, headers);
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PaymentResponse> response = restTemplate.exchange(INITIALIZE_DEPOSIT, HttpMethod.POST, entity, PaymentResponse.class);
            log.info(Objects.requireNonNull(response.getBody()).toString());

            return new ResponseEntity<>(response.getBody().getData().getAuthorization_url(),HttpStatus.ACCEPTED);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("Failed to initiate transaction", e.getStatusCode());
        }
    }

    public ResponseEntity<String> verifyPayment(String reference, String transactionType) {
        User user = userRepository.findByEmail(userEmail).get();
        wallet = walletRepository.findByUser_UserId(user.getUserId());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ SECRET_KEY);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(VERIFY_URL + reference, HttpMethod.GET, entity, String.class);
            if(response.getStatusCodeValue()==200){
                log.info(response.getBody());
                System.out.println(response);
                if(transactionType.equalsIgnoreCase("makepayment")){
                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/")).build();

                }else{
                //UPDATE WALLET AND WALLET TRANSACTION IN DATABASE
                wallet.setAccountBalance(wallet.getAccountBalance().add(fundingAmount));
                walletRepository.save(wallet);

                Transaction walletTransaction = Transaction.builder()
                        .name(user.getFirstName() + " " + user.getLastName())
                        .bankCode("CARD")
                        .wallet(wallet)
                        .transactionType(FUNDWALLET)
                        .transactionStatus(TransactionStatus.SUCCESS)
                        .amount(fundingAmount)
                        .transactionReference(paymentReference)
                        .build();
                walletTransactionRepository.save(walletTransaction);
                    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/pay-buddy/dashboard")).build();

                }
            }
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/")).build();

        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/")).build();
        }
    }
}


