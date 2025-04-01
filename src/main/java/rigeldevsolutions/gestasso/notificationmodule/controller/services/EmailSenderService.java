package rigeldevsolutions.gestasso.notificationmodule.controller.services;


import org.springframework.scheduling.annotation.Async;
import rigeldevsolutions.gestasso.notificationmodule.model.dto.EmailAttachment;

import java.util.List;

public interface EmailSenderService
{
    @Async
    void sendEmailWithAttachments(String senderMail, String receiverMail, String mailObject, String message, List<EmailAttachment> attachments) throws IllegalAccessException;
    void sendEmail(String senderMail, String receiverMail, String mailObject, String message) throws IllegalAccessException;
}
