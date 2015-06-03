package fr.sii.repository.email;

import fr.sii.config.email.EmailingSettings;
import fr.sii.domain.email.Email;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
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
    }

    public void send(Email email) throws Exception {

        if(emailingSettings.isSend() == false)
            return;
        Templating t = new Templating(email.getTemplate());
        t.setData(email.getData());

        Session session = Session.getInstance(props,null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailingSettings.getEmailSender()));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email.getTo()));
            message.setSubject(MimeUtility.encodeText(email.getSubject(), "utf-8", "B"));
            message.setContent(t.getTemplate(), "text/html");
            Transport.send(message);
    }
}
