package com.antique.auction.email.impl;

import com.antique.auction.email.EmailService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Value("${send.email}")
    public boolean sendEmail;

    public EmailServiceImpl(@Qualifier("getJavaMailSender") JavaMailSender emailSender, SpringTemplateEngine thymeleafTemplateEngine) {
        this.emailSender = emailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Override
    public void sendAwardedEmail(
            String to, String subject, Map<String, Object> templateModel)
            throws MessagingException {

        String htmlBody = createHtml(templateModel, "awarded-email.html");

        sendHtmlMessage(to, subject, htmlBody);
    }

    @Override
    public void sendFinishBidEmail(
            String to, String subject, Map<String, Object> templateModel)
            throws MessagingException {

        String htmlBody = createHtml(templateModel, "bid-finished-email.html");

        sendHtmlMessage(to, subject, htmlBody);
    }

    @Override
    public void sendNewBidOnItemEmail(
            String to, String subject, Map<String, Object> templateModel)
            throws MessagingException {

        String htmlBody = createHtml(templateModel, "new-bid-email.html");

        sendHtmlMessage(to, subject, htmlBody);
    }

    private String createHtml(Map<String, Object> templateModel, String s) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        return thymeleafTemplateEngine.process(s, thymeleafContext);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("antique.auction.mail.sender@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        if (sendEmail) {
            emailSender.send(message);
        }
    }
}
