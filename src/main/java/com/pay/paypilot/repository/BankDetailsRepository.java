package com.pay.paypilot.repository;


import com.pay.paypilot.model.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {
    BankDetails findByAccountNumberAndBankCode(String accountNumber, String bankCode);
}
