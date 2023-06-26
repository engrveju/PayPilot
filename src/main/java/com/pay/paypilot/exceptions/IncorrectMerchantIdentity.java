package com.pay.paypilot.exceptions;

public class IncorrectMerchantIdentity extends RuntimeException{

    public IncorrectMerchantIdentity(String message) {
        super(message);
    }
}
