package com.pay.paypilot.controller;

import com.pay.paypilot.service.TransactionService;
import com.pay.paypilot.service.vtpass.pojos.response.data.TransactionResponseViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionController {
    private final TransactionService transactionService;
    @GetMapping("/viewTransaction")
    public ResponseEntity<TransactionResponseViewModel> viewTransaction(
            @RequestParam(name = "page", defaultValue = "0")
            int page, @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(transactionService.viewWalletTransaction(page, limit));
    }
}
