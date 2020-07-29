package com.antique.auction.email;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ComponentScan(basePackages = { "com.antique.auction.email" })
public class EmailConfiguration {

    @Value("${spring.mail.host}")
    public String emailHost;
    @Value("${spring.mail.port}")
    public int emailPort;
    @Value("${spring.mail.username}")
    public String emailUserName;
    @Value("${spring.mail.password}")
    public String emailPassword;
    @Value("${spring.mail.protocol}")
    public String emailProtocol;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailHost);
        mailSender.setPort(emailPort);

        mailSender.setUsername(emailUserName);
        mailSender.setPassword(emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("This is the test email template for your email:\n%s\n");
        return message;
    }

}
