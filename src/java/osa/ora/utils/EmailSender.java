package osa.ora.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import osa.ora.spi.IConstant;

/*
 * @author Osama Oransa
 */
public class EmailSender {

    private String smtpServerIP;
    private String smtpServerUser;
    private String smtpServerPassword;
    private String smtpServerPort;
    private boolean useSSL = false;

    /**
     * private constructor
     */
    public EmailSender(Hashtable<String,String> configurations) {
        smtpServerIP = configurations.get(IConstant.EMAIL_SERVER);
        smtpServerPort = configurations.get(IConstant.EMAIL_PORT);
        smtpServerUser = configurations.get(IConstant.EMAIL_USER);
        smtpServerPassword = configurations.get(IConstant.EMAIL_PASSWORD);
        this.useSSL ="TRUE".equalsIgnoreCase(configurations.get(IConstant.EMAIL_SSL));
    }

    /**
     * this public method used to send the mail in a thread
     * @param toUserEmail
     * @param subject
     * @param body
     * @param myImage
     * @param file
     * @param isPicture
     */
    public void sendMail(final String[] toUserEmail, final String subject, final String body,
            final BufferedImage myImage, final File file, final boolean isPicture) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", smtpServerIP);
                    props.put("mail.smtp.port", smtpServerPort);
                    props.put("mail.smtp.auth", "true");
                    //props.put("mail.debug", "false");
                    props.put("mail.smtp.socketFactory.port", smtpServerPort);
                    //next attributes for google gmail for SSL
                    if (useSSL) {
                        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                        props.put("mail.smtp.socketFactory.fallback", "false");
                    }
                    Authenticator auth = new SMTPAuthenticator();
                    Session session = Session.getInstance(props, auth);
                    //session.setDebug(false);
                    // create a message
                    Message msg = new MimeMessage(session);
                    msg.setHeader("Exam System", "By Osama Oransa");
                    // set the from and to address
                    InternetAddress addressFrom = new InternetAddress(smtpServerUser);
                    msg.setFrom(addressFrom);
                    InternetAddress[] addressTo = new InternetAddress[toUserEmail.length];
                    for (int i = 0; i < toUserEmail.length; i++) {
                        addressTo[i] = new InternetAddress(toUserEmail[i]);
                    }
                    msg.setRecipients(Message.RecipientType.TO, addressTo);
                    String[] messageParts = body.split("##");
                    //no thing attached
                    if (messageParts.length == 1) {
                        msg.setContent(body, "text/html");
                        //image is attached
                        //body is text and image
                    } else if (messageParts.length == 2) {
                        MimeMultipart multipart = new MimeMultipart();//"related");
                        MimeBodyPart messageBodyPart;
                        // first part html text before image
                        messageBodyPart = new MimeBodyPart();
                        if (isPicture) {
                            messageBodyPart.setContent(messageParts[0] + "<br><img src=\"cid:image\"><br>" + messageParts[1], "text/html");
                            multipart.addBodyPart(messageBodyPart);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            boolean readingImageResult;
                            try {
                                readingImageResult = ImageIO.write(myImage, "jpg", byteArrayOutputStream);
                                System.out.println("Result of writing the image is " + readingImageResult);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                            DataSource fds = new ByteArrayDataSource(byteArray, "image/jpg");
                            messageBodyPart2.setDataHandler(new DataHandler(fds));
                            messageBodyPart2.setHeader("Content-ID", "<image>");
                            multipart.addBodyPart(messageBodyPart2);
                            msg.setContent(multipart);
                        } else {
                            //is file attached, like voice....
                            messageBodyPart.setContent(messageParts[0] + "<br>" + messageParts[1], "text/html");
                            multipart.addBodyPart(messageBodyPart);
                            // Part two is attachment
                            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                            DataSource source = new FileDataSource(file);
                            messageBodyPart2.setDataHandler(new DataHandler(source));
                            messageBodyPart2.setFileName(file.getName());
                            multipart.addBodyPart(messageBodyPart2);
                            msg.setContent(multipart);
                        }
                    }
                    // Setting the Subject and Content Type
                    msg.setSubject(subject);
                    Transport.send(msg);
                    System.out.println("Message sent to " + toUserEmail + " OK.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Exception " + ex);
                }
            }
        }.start();
    }
    // Also include an inner class that is used for authentication purposes
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            System.out.println("call authentication");
            String username = smtpServerUser;
            String password = smtpServerPassword;
            return new PasswordAuthentication(username, password);
        }
    }
    /**
     * this method used to replace parameters in the message
     * @param params array of parameters to replace
     * @param message the message body to replace
     */
    public static String replaceParams(String[] params, String message1) {
        if (message1 == null) {
            return null;
        }
        StringBuffer message = new StringBuffer(message1);
        String paramNo = "";
        if (params != null && params.length > 0) {
            System.out.println("will replace " + params.length + " parameters");
            for (int i = 0; i < params.length; i++) {
                paramNo = "%" + i;
                int place = message.indexOf(paramNo);
                if (place != -1) {
                    message = message.replace(place, place + 2, params[i]);
                }
            }
        } else {
            System.out.println("No parameters sent to replace");
        }
        return message.toString();
    }

}
