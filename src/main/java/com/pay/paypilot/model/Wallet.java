package com.pay.paypilot.model;


import com.pay.paypilot.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallet_tbl")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;
    private String accountNumber;
    private BigDecimal accountBalance;
    private String pin;
    private boolean isPinUpdated;
    @OneToOne
    @JoinColumn(name = "user_user_id")
    private User user;
    private WalletStatus status;
    @OneToMany(mappedBy = "wallet")
    private List<BankDetails> bankDetails;
    @CreationTimestamp
    private LocalDate createdAt;
    @UpdateTimestamp
    private LocalDate lastTransactionDate;
    @OneToMany (mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}
