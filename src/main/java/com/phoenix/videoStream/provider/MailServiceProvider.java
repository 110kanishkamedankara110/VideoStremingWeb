package com.phoenix.videoStream.provider;

import com.phoenix.videoStream.mail.Mailable;
import com.phoenix.videoStream.util.Env;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

import java.util.Properties;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MailServiceProvider {
    private ThreadPoolExecutor executor;
    private Authenticator authenticator;
    private Properties properties = new Properties();
    private static MailServiceProvider mailServiceProvider;
    private BlockingDeque<Runnable> blockingDeque = new LinkedBlockingDeque();

    private MailServiceProvider() {
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", Env.get("mail.host"));
        properties.put("mail.smtp.port", Env.get("mail.port"));
    }

    public static MailServiceProvider getInstance() {
        if (mailServiceProvider == null) {
            mailServiceProvider = new MailServiceProvider();
        }
        return mailServiceProvider;
    }

    public void start() {
        authenticator = new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Env.get("mail.userName"), "mail.password");
            }
        };
        executor = new ThreadPoolExecutor(2, 5, 5, TimeUnit.SECONDS, blockingDeque, new ThreadPoolExecutor.AbortPolicy());
        executor.prestartAllCoreThreads();
        System.out.println("MailServiceProvider : Running........");
    }

    public Properties getProperties() {
        return properties;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    public void sendMail(Mailable mailable) {
        this.blockingDeque.offer(mailable);
        System.out.println("Added Mail");
    }
}
