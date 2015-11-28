package fr.sii.service.email;

import fr.sii.config.email.EmailingSettings;
import fr.sii.domain.email.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailingService {

    @Autowired
    private EmailingSettings emailingSettings;

    @Autowired
    private JavaMailSender mailSender;

    public void send(Email e) throws Exception {
        if(!emailingSettings.isSend()) return;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Templating t = new Templating(e.getTemplate());
        t.setData(e.getData());

        helper.setFrom(emailingSettings.getEmailSender());
        helper.setTo(e.getTo());
        helper.setBcc(e.getBcc().toArray(new String[e.getBcc().size()]));
        helper.setSubject(e.getSubject());
        helper.setText(t.getTemplate(), true);

        mailSender.send(message);
    }
}
