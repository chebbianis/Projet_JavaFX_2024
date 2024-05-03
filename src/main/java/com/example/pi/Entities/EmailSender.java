package com.example.pi.Entities;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;

public class EmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "malekbdiri06@gmail.com";
    private static final String EMAIL_PASSWORD = "eoaq tsch zyks xcfm";
    private static final Session session = createSession();

    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });
    }
    public static void sendWelcomeEmailWithSignature(String recipientEmail, String nom) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Rappel Visite");
            Multipart multipart = new MimeMultipart();
            String emailContentWithSignature = "<html>" +
                    "<body>" +
                    "<p>Cher " + nom + ",</p>" +
                    "<p>Ceci est un email de rappel pour vous informer que votre rendez-vous de visite est prévu pour demain</p>" +
                    "<p>Cordialement,<br></p>" +
                    "</body>" +
                    "</html>";
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(emailContentWithSignature, "text/html");
            multipart.addBodyPart(textPart);




            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }



}
