package com.unist.hexa.chickenq.mail;

/**
 * Created by ichung-gi on 2015. 8. 20..
 */

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.StrictMode;
import android.util.Log;

public class emailClient
{
    private String mMailHost = "smtp.gmail.com";
    private Session mSession;

    public emailClient(String user, String pwd)
    {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", mMailHost);
        mSession = Session
                .getInstance(props, new EmailAuthenticator(user, pwd));
    } // constructor

    public void sendMail(String subject, String body, String sender,
                         String recipients)
    {
        if(android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }

        try
        {
            Message msg = new MimeMessage(mSession);
            msg.setFrom(new InternetAddress(sender));
            Log.d("test", subject);
            msg.setSubject(subject);
            Log.d("test", body);
            msg.setContent(body, "text/html;charset=EUC-KR");
            msg.setSentDate(new Date());
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(msg);
        } catch (Exception e)
        {
            Log.d("Chung", "Exception occured : ");
            Log.d("Chung", e.toString());
            Log.d("Chung", e.getMessage());
        } // try-catch
    } // vodi sendMail

    class EmailAuthenticator extends Authenticator
    {
        private String id;
        private String pw;
        public EmailAuthenticator(String id, String pw)
        {
            super();
            this.id=id;
            this.pw=pw;
        } //constructor

        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(id, pw);
        } //PasswordAuthentication getPasswordAuthentication
    } //class EmailAuthenticator
} // class emailClient