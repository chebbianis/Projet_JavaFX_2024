package com.example.pi.Services;

import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class GmailService {

    public static void SendEmail(String email, String emailMessage, String object, String filePath) throws IOException {
        final String from = "testesenpfe@gmail.com"; // from address. As this is using Gmail SMTP.
        final String password = "wokd lygo kcxe cjbg"; // password for from mail address.

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(object);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(emailMessage, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            // Vérifie si le chemin du fichier n'est pas vide avant de l'attacher au message
            if (!filePath.isEmpty()) {
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.attachFile(new File(filePath));
                multipart.addBodyPart(attachmentBodyPart);
            }

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Mail successfully sent...");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Mail successfully sent...");
            alert.show();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
