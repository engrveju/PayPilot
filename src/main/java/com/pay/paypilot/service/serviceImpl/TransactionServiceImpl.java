package com.pay.paypilot.service.serviceImpl;

import com.pay.paypilot.model.Transaction;
import com.pay.paypilot.model.Wallet;
import com.pay.paypilot.repository.TransactionRepository;
import com.pay.paypilot.repository.UserRepository;
import com.pay.paypilot.repository.WalletRepository;
import com.pay.paypilot.service.TransactionService;
import com.pay.paypilot.service.vtpass.pojos.response.data.TransactionResponse;
import com.pay.paypilot.service.vtpass.pojos.response.data.TransactionResponseViewModel;
import com.pay.paypilot.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserUtil userUtil;
    @Override
    public TransactionResponseViewModel viewWalletTransaction(int page, int limit) {
        String userEmail = userUtil.getAuthenticatedUserEmail();
        userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));
        Wallet wallet = walletRepository.findWalletByUser_Email(userEmail);
        if (page > 0) page -= 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.by("transactionId").descending());
        Page<Transaction> pageList = transactionRepository.findAllByWallet(wallet, pageable);
        List<Transaction> transactionList = pageList.getContent();
        List<TransactionResponse> transactionResponse = TransactionResponse.mapFromTransaction(transactionList);
        return TransactionResponseViewModel.builder()
                .list(transactionResponse)
                .count(pageList.getNumberOfElements())
                .totalPage(pageList.getTotalPages())
                .build();
    }
}