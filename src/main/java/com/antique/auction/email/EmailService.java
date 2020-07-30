package com.antique.auction.email;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailService {
    public void sendSimpleMessage(String to, String subject, String text);

    void sendMessageUsingThymeleafTemplate(
            String to, String subject, Map<String, Object> templateModel)
            throws MessagingException;
}
