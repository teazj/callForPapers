package fr.sii.repository.email;

import java.util.Properties;

import org.springframework.stereotype.Component;

import fr.sii.config.email.EmailingSettings;
import fr.sii.domain.email.Email;

/**
 * Created by tmaugin on 02/04/2015.
 */
@Component
public class ProductionEmailingRepository implements EmailingRepository {
    private EmailingSettings emailingSettings;
    private Properties props;

    public void login(EmailingSettings emailingSettings)
    {
        this.emailingSettings = emailingSettings;
        this.props = new Properties();
    }

    public void send(Email email) throws Exception {

        //TODO replace mail sending code
        /*
        if(!emailingSettings.isSend())
            return;
        Templating t = new Templating(email.getTemplate());
        t.setData(email.getData());

        Session session = Session.getInstance(props,null);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailingSettings.getEmailSender()));
        try {
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email.getTo()));
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }

        if(!email.getBcc().isEmpty()) {
            List<Address> bccAddresses = new ArrayList<>();
            for(String address : email.getBcc()) {
                try {
                    bccAddresses.add(new InternetAddress(address) {
                    });
                } catch (AddressException e) {
                    e.printStackTrace();
                }
            }
            message.addRecipients(Message.RecipientType.BCC, bccAddresses.toArray(new Address[bccAddresses.size()]) );
        }
        message.setSubject(MimeUtility.encodeText(email.getSubject(), "utf-8", "B"));
        message.setContent(t.getTemplate(), "text/html");
        Transport.send(message);
        */
    }
}
