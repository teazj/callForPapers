package fr.sii.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

import java.util.Properties;

/**
 * Created by tmaugin on 02/04/2015.
 */
public class ProductionEmailingRepository implements EmailingRepository {
    private EmailingSettings emailingSettings;
    private Properties props;

    public void login(EmailingSettings emailingSettings)
    {
        this.emailingSettings = emailingSettings;
        this.props = new Properties();
        this.props.put("mail.smtp.auth", "true");
        this.props.put("mail.smtp.starttls.enable", "true");
        this.props.put("mail.smtp.host", emailingSettings.getSmtphost());
        this.props.put("mail.smtp.port", emailingSettings.getSmtpport());
    }

    public void send(EmailModel email) throws Exception {
        Templating t = new Templating("./src/main/resources/mail/template/" + email.getTemplate());
        t.setData(email.getData());

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailingSettings.getUsername(), emailingSettings.getPassword());
                    }
                });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getFrom()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo()));
            message.setSubject(MimeUtility.encodeText(email.getSubject(), "utf-8", "B"));
            message.setContent(t.getTemplate(), "text/html");
            Transport.send(message);
    }
}
