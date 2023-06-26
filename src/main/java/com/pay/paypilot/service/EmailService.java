package com.pay.paypilot.service;

import com.pay.paypilot.dtos.requests.EmailSenderDto;

public interface EmailService {
    void sendMail(EmailSenderDto emailSenderDto);

}
