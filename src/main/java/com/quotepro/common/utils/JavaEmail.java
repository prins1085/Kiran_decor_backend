/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quotepro.common.utils;

import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

public class JavaEmail {
	
	private Session session;
   

    public static String sendEmail(String to, String Subject, String Body, byte[] attachment) {

        try {
           Properties emailProperties = System.getProperties();
            emailProperties.put("mail.smtp.port", "587");
            emailProperties.put("mail.smtp.auth", "true");
            emailProperties.put("mail.smtp.starttls.enable", "true");

            String emailHost = "smtp.gmail.com";
            String fromUser = Const.getListCPANEL().get("EMAIL").toString();//just the id alone without @gmail.com
            String fromUserEmailPassword = Const.getListCPANEL().get("PASSWORD").toString();

            String[] toEmails = to.split(",");

            Session mailSession = Session.getDefaultInstance(emailProperties, null);
            MimeMessage emailMessage = new MimeMessage(mailSession);

            for (int i = 0; i < toEmails.length; i++) {
                emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
            }

            emailMessage.setSubject(Subject);
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(Body);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            ByteArrayDataSource bds = new ByteArrayDataSource(attachment, "application/pdf"); 
         //   DataSource source = new FileDataSource(attachment);
            messageBodyPart.setDataHandler(new DataHandler(bds));
            messageBodyPart.setFileName("Report");
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts 
            emailMessage.setContent(multipart);

            Transport transport = mailSession.getTransport("smtp");

            transport.connect(emailHost, fromUser, fromUserEmailPassword);
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
            return "S";
        } catch (Throwable t) {
            return t.getMessage();
        }
    }

}
