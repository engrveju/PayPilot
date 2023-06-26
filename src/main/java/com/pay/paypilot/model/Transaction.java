package com.pay.paypilot.model;


import com.pay.paypilot.enums.TransactionStatus;
import com.pay.paypilot.enums.TransactionType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "transaction_tbl")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,unique = true)
    private Long transactionId;
    private BigDecimal amount;
    private String name;
    private String transactionReference;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @CreationTimestamp
    private LocalDate dateOfTransaction;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    private String description;
    @ManyToOne
    @JoinColumn(name = "wallet_wallet_id")
    private Wallet wallet;


}
