package osa.ora.beans;

import java.io.Serializable;
/*
 * @author Osama Oransa
 */
public class Notification implements Serializable {

    private int notificationId;
    private String[] params;
    private String fromMail;
    private String recipientsMails[];
    private String subject;
    private String messageBody;
    public Notification() {
    }

    public String[] getRecipientsMails() {
        return recipientsMails;
    }

    public void setRecipientsMails(String[] recipientsMails) {
        this.recipientsMails = recipientsMails;
    }

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the messageBody
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * @param messageBody the messageBody to set
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
