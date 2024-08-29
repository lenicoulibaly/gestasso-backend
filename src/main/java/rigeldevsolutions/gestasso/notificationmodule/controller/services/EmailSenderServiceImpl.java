package rigeldevsolutions.gestasso.notificationmodule.controller.services;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import rigeldevsolutions.gestasso.authmodule.controller.repositories.AccountTokenRepo;
import rigeldevsolutions.gestasso.authmodule.controller.services.spec.IJwtService;
import rigeldevsolutions.gestasso.authmodule.model.constants.SecurityConstants;
import rigeldevsolutions.gestasso.authmodule.model.entities.AccountToken;
import rigeldevsolutions.gestasso.authmodule.model.entities.AppUser;
import rigeldevsolutions.gestasso.authmodule.model.events.AccountActivationTokenCreatedEvent;
import rigeldevsolutions.gestasso.notificationmodule.controller.dao.EmailNotificationRepo;
import rigeldevsolutions.gestasso.notificationmodule.model.dto.EmailAttachment;
import rigeldevsolutions.gestasso.notificationmodule.model.entities.EmailNotification;
import rigeldevsolutions.gestasso.reportmodule.service.IServiceReport;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService
{
    private final JavaMailSender javaMailSender;
    private final HTMLEmailBuilder htmlEmailBuilder;
    private final EmailServiceConfig emailServiceConfig;
    private final IServiceReport reportService;
    @Value("${auth.server.address}")
    private String authServerAddress;
    @Value("${gestasso.server.address}")
    private String synchronreAdress;
    @Value("${front.adress}")
    private String frontAddress;
    private final EmailBodyBuilder emailBodyBuilder;
    private final IJwtService jwtService;
    private final EmailNotificationRepo emailRepo;
    private final AccountTokenRepo tokenRepo;

    @Override @Async
    public void sendEmailWithAttachments(String senderMail, String receiverMail, String mailObject, String message, List<EmailAttachment> attachments) throws IllegalAccessException {
        String connectedUserEmail = jwtService.extractUsername();
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setText(message, true); // Second parameter true means that the message will be an HTML message
            mimeMessageHelper.setTo(receiverMail);
            mimeMessageHelper.addCc(connectedUserEmail);
            mimeMessage.setSubject(mailObject);
            mimeMessage.setFrom(senderMail);
            // Add attachments to the email
            if (attachments != null && !attachments.isEmpty()) {
                for (EmailAttachment attachment : attachments) {
                    DataSource dataSource = new ByteArrayDataSource(attachment.getContent(), attachment.getContentType());
                    mimeMessageHelper.addAttachment(attachment.getFilename(), dataSource);
                }
            }
            //MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalAccessException("Error while sending email");
        }
    }

    @Override
    @Async
    public void sendEmail(String senderMail, String receiverMail, String mailObject, String message) throws IllegalAccessException
    {
        try
        {
            MimeMessage  mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(message, true); // Second parameter true means that the message will be an HTML message
            mimeMessageHelper.setTo(receiverMail);
            mimeMessage.setSubject(mailObject);
            mimeMessage.setFrom(senderMail);
            javaMailSender.send(mimeMessage);
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
            throw new IllegalAccessException("Error while sending email");
        }
    }

    @Override
    public void sendReinitialisePasswordEmail(String receiverMail, String recipientUsername, String link) throws IllegalAccessException
    {
        this.sendEmail(emailServiceConfig.getSenderEmail(), receiverMail, SecurityConstants.PASSWORD_REINITIALISATION_REQUEST_OBJECT, htmlEmailBuilder.buildPasswordReinitialisationHTMLEmail(recipientUsername, frontAddress + link));
    }

    @Override
    public void sendAccountActivationEmail(String receiverMail, String recipientUsername, String activationLink) throws IllegalAccessException
    {
        this.sendEmail(emailServiceConfig.getSenderEmail(), receiverMail, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, htmlEmailBuilder.buildAccountActivationHTMLEmail(recipientUsername, frontAddress + activationLink));
    }

    @Override @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAccountActivationTokenCreated(AccountActivationTokenCreatedEvent event) throws IllegalAccessException
    {
        System.out.println("Processing account activation token for user: " + event.getUser().getEmail());
        AppUser user = event.getUser(); AccountToken accountToken = event.getAccountToken();
        this.sendAccountActivationEmail(user.getEmail(), user.getFirstName(), frontAddress + emailServiceConfig.getActivateAccountLink() + "/" + accountToken.getToken());
        EmailNotification emailNotification = new EmailNotification(user, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), jwtService.getConnectedUserId());
        emailNotification.setSent(true);
        emailNotification = emailRepo.save(emailNotification);
        BeanUtils.copyProperties(event.getAi(), emailNotification);
        accountToken.setEmailSent(true);
        tokenRepo.save(accountToken);
    }
}