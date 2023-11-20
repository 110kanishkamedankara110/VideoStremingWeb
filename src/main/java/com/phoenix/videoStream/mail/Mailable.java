package com.phoenix.videoStream.mail;

import com.phoenix.videoStream.provider.MailServiceProvider;
import com.phoenix.videoStream.util.Env;
import io.rocketbase.mail.EmailTemplateBuilder;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public abstract class Mailable implements Runnable {
    @Override
    public void run() {
//        try {
//            Session session = Session.getInstance(mailServiceProvider.getProperties(), mailServiceProvider.getAuthenticator());
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(Env.get("app.Mail")));
//            build(message);
////            System.out.println(message.getRecipients(Message.RecipientType.TO).length);
//            if (message.getRecipients(Message.RecipientType.TO).length > 0) {
//                Transport.send(message);
//                System.out.println("Sent");
//            } else {
//                System.out.println("no Recipient");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String to = "110kqnishkamedankara110@gmail.com";
        //provide sender's email ID
        String from = Env.get("app.Mail");
        //provide Mailtrap's username
        final String username = Env.get("mail.userName");
        //provide Mailtrap's password
        final String password = Env.get("mail.password");
        //provide Mailtrap's host address
        String host = Env.get("mail.host");
        //configure Mailtrap's SMTP server details


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");


        //create the Session object
        Session session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            //create a MimeMessage object
            Message message = new MimeMessage(session);
            //set From email field
            message.setFrom(new InternetAddress(from));
            //set To email field
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //set email subject field
            message.setSubject("Here comes Jakarta Mail!");
            //set the content of the email message
            message.setText("Just discovered that Jakarta Mail is fun and easy to use");
            //send the email message
            Transport.send(message);
            System.out.println("Email Message Sent Successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void build(Message message)throws MessagingException;

    private MailServiceProvider mailServiceProvider;
private EmailTemplateBuilder.EmailTemplateConfigBuilder emailTemplateConfigBuilder;
    public Mailable() {
        mailServiceProvider = MailServiceProvider.getInstance();
        emailTemplateConfigBuilder=EmailTemplateBuilder.builder();
    }

    public EmailTemplateBuilder.EmailTemplateConfigBuilder getEmailTemplateConfigBuilder(){
        return emailTemplateConfigBuilder;
    }
}
