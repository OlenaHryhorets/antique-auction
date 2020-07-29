package com.antique.auction.email;

public interface EmailService {
    public void sendSimpleMessage(String to, String subject, String text);
}
