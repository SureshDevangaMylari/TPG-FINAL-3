package base;


import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {

    public static void sendEmailWithAttachments (
            String[] toEmails,
            String[] ccEmails,
            String subject,
            String body,
            File[] attachments
    ) throws Exception{
    	
       final String fromEmail = DriverManager.getpropertyvalue("fromEmail");
       final String password =  DriverManager.getpropertyvalue("fromEmailPassword");
       System.out.println("======== Inside EmailSender Class =======");
       System.out.println("From email address: "+fromEmail);
       System.out.println("Password for from Email address: "+password);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // ✅ Correct Session (jakarta.mail.Session)
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

       
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));

            // ✅ TO recipients
            if (toEmails != null && toEmails.length > 0) {
                InternetAddress[] toAddresses = new InternetAddress[toEmails.length];
                for (int i = 0; i < toEmails.length; i++) {
                    toAddresses[i] = new InternetAddress(toEmails[i]);
                }
                message.setRecipients(Message.RecipientType.TO, toAddresses);
            }

            // ✅ CC recipients (safe check)
            if (ccEmails != null && ccEmails.length > 0) {
                InternetAddress[] ccAddresses = new InternetAddress[ccEmails.length];
                for (int i = 0; i < ccEmails.length; i++) {
                    ccAddresses[i] = new InternetAddress(ccEmails[i]);
                }
                message.setRecipients(Message.RecipientType.CC, ccAddresses);
            }

            message.setSubject(subject);

            // ✅ Email body
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            // ✅ Attachments (safe check)
            if (attachments != null && attachments.length > 0) {
                for (File file : attachments) {
                    if (file != null && file.exists()) {
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        attachmentPart.attachFile(file);
                        multipart.addBodyPart(attachmentPart);
                    }
                }
            }

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("✅ Email sent successfully with multiple attachments.");

       
    }
}