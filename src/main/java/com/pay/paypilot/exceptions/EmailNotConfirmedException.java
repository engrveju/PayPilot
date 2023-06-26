package com.pay.paypilot.exceptions;

/**
 * @author Emmanuel Ugwueze
 * @created 25/06/2023 - 23:38
 * @project Pay-Pilot
 */

public class EmailNotConfirmedException extends RuntimeException{
    public EmailNotConfirmedException(String message) {
        super(message);
    }
}
