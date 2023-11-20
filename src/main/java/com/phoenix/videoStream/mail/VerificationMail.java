package com.phoenix.videoStream.mail;

import io.rocketbase.mail.model.HtmlTextEmail;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;

public class VerificationMail extends Mailable{

    private String to;
    private String verificationCode;
    public VerificationMail(String to,String verificationCode){
        this.to=to;
        this.verificationCode = verificationCode;
    }

    @Override
    public void build(Message message) throws MessagingException {
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(this.to));
        HtmlTextEmail htmlTextEmail = getEmailTemplateConfigBuilder()
                .header()
                .logo("https://www.rocketbase.io/img/logo-dark.png").logoHeight(41)
                .and()
                .text("Welcome, {{name}}!").h1().center().and()
                .text("Thanks for trying [Product Name]. We’re thrilled to have you on board. To get the most out of [Product Name], do this primary next step:").and()
                .button("Do this Next", "http://localhost").blue().and()
                .text("For reference, here's your login information:").and()
                .attribute()
                .keyValue("Login Page", "{{login_url}}")
                .keyValue("Username", "{{username}}")
                .and()
                .html("If you have any questions, feel free to <a href=\"mailto:{{support_email}}\">email our customer success team</a>. (We're lightning quick at replying.) We also offer <a href=\"{{live_chat_url}}\">live chat</a> during business hours.",
                        "If you have any questions, feel free to email our customer success team\n" +
                                "(We're lightning quick at replying.) We also offer live chat during business hours.").and()
                .text("Cheers,\n" +
                        "The [Product Name] Team").and()
                .copyright("rocketbase").url("https://www.rocketbase.io").suffix(". All rights reserved.").and()
                .footerText("[Company Name, LLC]\n" +
                        "1234 Street Rd.\n" +
                        "Suite 1234").and()
                .footerImage("https://cdn.rocketbase.io/assets/loading/no-image.jpg").width(100).linkUrl("https://www.rocketbase.io").and()
                .build();

        message.setContent(htmlTextEmail.getHtml(),"text/html");
        message.setContent("<h1>Working</h1>","text/html");


    }
}
