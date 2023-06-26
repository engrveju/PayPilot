package com.pay.paypilot.exceptions;

public class WalletTransactionException extends RuntimeException{

    public WalletTransactionException(String message) {
        super(message);
    }
}
