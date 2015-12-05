package fr.sii.service.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import fr.sii.config.email.EmailingSettings;
import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.email.Email;
import fr.sii.dto.CommentUser;
import fr.sii.dto.TalkAdmin;
import fr.sii.dto.TalkUser;
import fr.sii.entity.AdminUser;
import fr.sii.entity.User;
import fr.sii.service.admin.user.AdminUserService;

@Service
public class EmailingService {

    private final Logger log = LoggerFactory.getLogger(EmailingService.class);

    private final GlobalSettings globalSettings;

    private final EmailingSettings emailingSettings;

    private final AdminUserService adminUserServiceCustom;

    private final JavaMailSender mailSender;

    @Autowired
    public EmailingService(GlobalSettings globalSettings, EmailingSettings emailingSettings, AdminUserService adminUserServiceCustom,
            JavaMailSender mailSender) {
        super();
        this.globalSettings = globalSettings;
        this.emailingSettings = emailingSettings;
        this.adminUserServiceCustom = adminUserServiceCustom;
        this.mailSender = mailSender;
    }

    private void send(Email email) {
        if (!emailingSettings.isSend())
            return;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        Templating templating = new Templating(email.getTemplate());
        templating.setData(email.getData());

        try {
            helper.setFrom(emailingSettings.getEmailSender());
            // To address must not be null
            if (email.getTo() == null) {
                helper.setTo(emailingSettings.getEmailSender());
            } else {
                helper.setTo(email.getTo());
            }
            helper.setBcc(email.getBcc().toArray(new String[email.getBcc().size()]));
            helper.setSubject(email.getSubject());
            helper.setText(templating.getTemplate(), true);

            mailSender.send(message);
            log.debug("Sent e-mail to User '{}'", email.getTo());
        } catch (MailSendException | MessagingException e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", email.getTo(), e.getMessage());
        }
    }

    /**
     * Send new message about talk.
     * 
     * @param talk
     * @param comment
     */
    public void sendNewMessage(User user, TalkAdmin talk, CommentUser comment) {
        log.debug("Sending new message e-mail to '{}'", user.getEmail());

        List<String> bcc = new ArrayList<>();
        for (AdminUser adminUser : adminUserServiceCustom.findAll()) {
            bcc.add(adminUser.getEmail());
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("name", user.getFirstname());
        map.put("talk", talk.getName());
        map.put("hostname", globalSettings.getHostname());
        map.put("id", String.valueOf(talk.getId()));

        Email email = new Email(user.getEmail(), "Vous avez un nouveau message à propos du talk " + talk.getName(), "mails/newMessage.html", map, bcc);
        send(email);
    }

    /**
     * Send new speaker message to admin.
     * 
     * @param user
     * @param talk
     */
    public void sendNewMessageAdmin(User user, TalkUser talk) {
        log.debug("Sending new message e-mail to '{}'", user.getEmail());

        List<String> bcc = new ArrayList<>();
        for (AdminUser adminUser : adminUserServiceCustom.findAll()) {
            bcc.add(adminUser.getEmail());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("name", user.getFirstname());
        map.put("talk", talk.getName());
        map.put("hostname", globalSettings.getHostname());
        map.put("id", String.valueOf(talk.getId()));

        Email email = new Email(null, "Le speaker " + user.getFirstname() + " " + user.getLastname() + " à écrit un message à propos du talk" + talk.getName(),
                "mails/newMessageAdmin.html", map, bcc);
        send(email);
    }

    /**
     * Send email validation.
     * 
     * @param user
     */
    public void sendEmailValidation(User user) {
        log.debug("Sending email validation e-mail to '{}'", user.getEmail());

        String link = globalSettings.getHostname() + "/#/verify?id=" + String.valueOf(user.getId()) + "&token=" + user.getVerifyToken();
        log.debug(link);

        HashMap<String, String> map = new HashMap<>();
        map.put("link", link);
        map.put("hostname", globalSettings.getHostname());

        Email email = new Email(user.getEmail(), "Confirmation de votre adresse e-mail", "mails/verify.html", map);
        send(email);
    }

    /**
     * Send Confirmation of your session.
     * 
     * @param user
     * @param talk
     */
    public void sendSessionConfirmation(User user, TalkUser talk) {
        log.debug("Sending email confirmation e-mail to '{}'", user.getEmail());

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", user.getFirstname());
        map.put("talk", talk.getName());
        map.put("hostname", globalSettings.getHostname());
        map.put("id", String.valueOf(talk.getId()));

        Email email = new Email(user.getEmail(), "Confirmation de votre session", "mails/confirmed.html", map);
        send(email);
    }
}
