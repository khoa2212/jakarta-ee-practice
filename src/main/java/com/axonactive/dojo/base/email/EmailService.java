package com.axonactive.dojo.base.email;

import com.axonactive.dojo.base.config.MailConfig;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import java.util.Properties;

@Stateless
public class EmailService {

    private static final String SEND_EMAIL_FAILED = "Send email failed";

    public void sendEmail(String receiveEmail, String displayName, String token) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", MailConfig.HOST_NAME);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", MailConfig.TSL_PORT);
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailConfig.APP_EMAIL, MailConfig.APP_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiveEmail));
            message.setSubject("No reply: Activate account - Department management website");

            String url = String.format("%s%s", MailConfig.VERIFY_URL, token);

            String htmlMessage = String.format("<p>Dear <b>%s</b>,</p>\n" +
                    "<p>This is an email sent from <b>Department management website</b></p>\n" +
                    "<p>Click here to activate your account: <a href='%s'>activate</a></p><br>\n" +
                    "<p>Regard,<br>Department management website,<br>Nguyen Tuan Khoa<br><br><i>Copyright © Nguyen Tuan Khoa</i></p>", displayName, url);

            message.setContent(htmlMessage, "text/html; charset=utf-8");

            Transport.send(message);
        }
        catch (MessagingException exception) {
            throw new ServerErrorException(SEND_EMAIL_FAILED, Response.Status.INTERNAL_SERVER_ERROR, exception);
        }
    }
}
