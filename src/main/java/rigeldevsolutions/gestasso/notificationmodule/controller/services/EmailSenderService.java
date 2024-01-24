package rigeldevsolutions.gestasso.notificationmodule.controller.services;


import org.springframework.scheduling.annotation.Async;
import rigeldevsolutions.gestasso.notificationmodule.model.dto.EmailAttachment;

import java.math.BigDecimal;
import java.util.List;

public interface EmailSenderService
{
    @Async
    void sendEmailWithAttachments(String senderMail, String receiverMail, String mailObject, String message, List<EmailAttachment> attachments) throws IllegalAccessException;
    void sendEmail(String senderMail, String receiverMail, String mailObject, String message) throws IllegalAccessException;
    void sendReinitialisePasswordEmail(String receiverMail, String recipientUsername, String link) throws IllegalAccessException;
    void sendAccountActivationEmail(String receiverMail, String recipientUsername, String activationLink) throws IllegalAccessException;

}
