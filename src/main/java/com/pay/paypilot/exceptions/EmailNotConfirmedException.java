package com.pay.paypilot.exceptions;


public class EmailNotConfirmedException extends RuntimeException{
    public EmailNotConfirmedException(String message) {
        super(message);
    }
}
