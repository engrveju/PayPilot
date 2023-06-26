package com.pay.paypilot.service.vtpass.pojos.response.data;


import com.pay.paypilot.enums.TransactionStatus;
import com.pay.paypilot.enums.TransactionType;
import com.pay.paypilot.model.Transaction;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TransactionResponse {
    private Long transactionId;
    private String referenceNumber;
    private String name;
    private String bankCode;
    private BigDecimal amount;
    private String transactionReference;
    private TransactionType transactionType;
    private LocalDate dateOfTransaction;
    private TransactionStatus transactionStatus;
    private String description;


    public static TransactionResponse mapFromTransaction (Transaction transaction){
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .name(transaction.getName())
                .amount(transaction.getAmount())
                .transactionReference(transaction.getTransactionReference())
                .transactionType(transaction.getTransactionType())
                .dateOfTransaction(transaction.getDateOfTransaction())
                .transactionStatus(transaction.getTransactionStatus())
                .description(transaction.getDescription())
                .build();
    }

    public static List<TransactionResponse> mapFromTransaction (List<Transaction> transactions){
        return  transactions.stream()
                .map(TransactionResponse::mapFromTransaction)
                .collect(Collectors.toList());
    }

}
