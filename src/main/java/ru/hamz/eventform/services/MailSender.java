//package ru.hamz.eventform.services;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//
//public class MailSender {
//    private final JavaMailSender mailSender;
//    private static final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//
//    @Value("${spring.mail.port}")
//    private static int port;
//
//    @Value("${spring.mail.host}")
//    private static String host;
//
//    @Value("${spring.mail.protocol}")
//    private static String protocol;
//
//    @Value("${mail.smtps.auth}")
//    private static String auth;
//
//
//    public MailSender(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    public void send(String emailFrom, String emailTo, String subject, String message) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        mailMessage.setFrom(emailFrom);
//        mailMessage.setTo(emailTo);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(message);
//
//        mailSender.send(mailMessage);
//    }
//
//    public static MailSender configureMailSender(String userName, String password) {
//        javaMailSender.setHost(host);
//        javaMailSender.setPort(port);
//        javaMailSender.setUsername(userName);
//        javaMailSender.setPassword(password);
//        javaMailSender.setProtocol(protocol);
//
//        Properties javaMailProperties = javaMailSender.getJavaMailProperties();
//        javaMailProperties.setProperty("mail.smtps.auth", auth);
//
//        return new MailSender(javaMailSender);
//    }
//}



package ru.hamz.eventform.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


public class MailSender {
    private final JavaMailSender mailSender;

    public MailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String emailFrom, String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
