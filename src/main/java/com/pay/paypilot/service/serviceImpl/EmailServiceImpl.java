package com.pay.paypilot.service.serviceImpl;

import com.pay.paypilot.dtos.requests.EmailSenderDto;
import com.pay.paypilot.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    @Override
    public void sendMail(EmailSenderDto emailSenderDto){
        if (
                (Objects.nonNull(emailSenderDto.getTo())) &&
                        (Objects.nonNull(emailSenderDto.getSubject())) &&
                        (Objects.nonNull(emailSenderDto.getContent()))
        ) {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");
            try {
                LOGGER.info("Transferring Data from EmailSenderDto to MimeMessage helper");
                message.setTo(emailSenderDto.getTo());
                message.setSubject(emailSenderDto.getSubject());
                message.setText(emailSenderDto.getContent(),true);
                emailSender.send(mimeMessage);
                LOGGER.info("Mail has been sent");
            } catch (Exception e) {
                LOGGER.error("An error occurred while sending an email to address : " + emailSenderDto.getTo() + "; error: " + e.getMessage());
            }
        }
    }
}
