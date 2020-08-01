package com.antique.auction.email;

import javax.mail.MessagingException;
import java.util.Map;

public interface EmailService {
    void sendAwardedEmail(
            String to, String subject, Map<String, Object> templateModel)
            throws MessagingException;
    void sendFinishBidEmail(
            String to, String subject, Map<String, Object> templateModel)
            throws MessagingException;
    void sendNewBidOnItemEmail(
            String to, String subject, Map<String, Object> templateModel)
            throws MessagingException;
}
