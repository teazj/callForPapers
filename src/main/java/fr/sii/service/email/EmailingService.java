package fr.sii.service.email;

import java.io.StringWriter;
import java.util.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import fr.sii.dto.ApplicationSettings;
import fr.sii.service.admin.config.ApplicationConfigService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import fr.sii.config.email.EmailingSettings.EmailType;
import fr.sii.config.email.EmailingSettings;

import fr.sii.config.global.GlobalSettings;
import fr.sii.dto.TalkAdmin;
import fr.sii.dto.TalkUser;
import fr.sii.entity.AdminUser;
import fr.sii.entity.User;
import fr.sii.service.admin.user.AdminUserService;

@Service
public class EmailingService {

    private final Logger log = LoggerFactory.getLogger(EmailingService.class);

    @Autowired
    ApplicationConfigService applicationConfigService;

    @Autowired
    private GlobalSettings globalSettings;

    @Autowired
    private EmailingSettings emailingSettings;

    @Autowired
    private AdminUserService adminUserServiceCustom;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * EmailingService constructor.
     */
    public EmailingService() {
        super();
    }

    /**
     * Send Confirmation of your session.
     *
     * @param user
     * @param talk
     * @param locale
     */
    @Async
    public void sendConfirmed(User user, TalkUser talk, Locale locale) {
        log.debug("Sending email confirmation e-mail to '{}'", user.getEmail());

        HashMap<String, String> map = new HashMap<>();
        map.put("name", user.getFirstname());
        map.put("talk", talk.getName());
        map.put("hostname", globalSettings.getHostname());
        map.put("id", String.valueOf(talk.getId()));

        String templatePath = emailingSettings.getTemplatePath(EmailType.CONFIRMED, locale);

        String content = processContent(templatePath, map);
        String subject = emailingSettings.getSubject(EmailType.CONFIRMED, locale);

        sendEmail(user.getEmail(), subject, content, null);
    }

    /**
     * Send new message about talk.
     *
     * @param talk
     * @param locale
     */
    @Async
    public void sendNewMessage(User user, TalkAdmin talk, Locale locale) {
        log.debug("Sending new message e-mail to '{}'", user.getEmail());

        // List<String> bcc = adminUserServiceCustom.findAll().stream().map(a -> a.getEmail()).collect(Collectors.toList());
        List<String> bcc = new ArrayList<>();
        for (AdminUser adminUser : adminUserServiceCustom.findAll()) {
            bcc.add(adminUser.getEmail());
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("name", user.getFirstname());
        map.put("talk", talk.getName());
        map.put("hostname", globalSettings.getHostname());
        map.put("id", String.valueOf(talk.getId()));

        String templatePath = emailingSettings.getTemplatePath(EmailType.NEW_MESSAGE, locale);

        String content = processContent(templatePath, map);
        String subject = emailingSettings.getSubject(EmailType.NEW_MESSAGE, locale, talk.getName());

        sendEmail(user.getEmail(), subject, content, bcc);
    }


    /**
     * Send new speaker message to admin.
     *
     * @param user
     * @param talk
     * @param locale
     */
    @Async
    public void sendNewMessageAdmin(User user, TalkUser talk, Locale locale) {
        log.debug("Sending new message admin e-mail to '{}'", user.getEmail());

        List<String> bcc = new ArrayList<>();
        for (AdminUser adminUser : adminUserServiceCustom.findAll()) {
            bcc.add(adminUser.getEmail());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("name", user.getFirstname());
        map.put("talk", talk.getName());
        map.put("hostname", globalSettings.getHostname());
        map.put("id", String.valueOf(talk.getId()));

        String templatePath = emailingSettings.getTemplatePath(EmailType.NEW_MESSAGE_ADMIN, locale);

        String content = processContent(templatePath, map);
        String subject = emailingSettings.getSubject(EmailType.NEW_MESSAGE_ADMIN, locale, talk.getName());

        // To address must not be null => from = to
        sendEmail(emailingSettings.getEmailSender(), subject, content, bcc);
    }

    /**
     * Send Confirmation of your session.
     *
     * @param user
     * @param talk
     * @param locale
     */
    @Async
    public void sendNotSelectionned(User user, TalkUser talk, Locale locale) {
        log.debug("Sending not sectioned e-mail to '{}'", user.getEmail());

        HashMap<String, String> map = new HashMap<>();
        map.put("name", user.getFirstname());
        map.put("talk", talk.getName());
        map.put("hostname", globalSettings.getHostname());
        map.put("community", applicationConfigService.getAppConfig().getCommunity());
        map.put("event", applicationConfigService.getAppConfig().getEventName());

        String templatePath = emailingSettings.getTemplatePath(EmailType.NOT_SELECTIONNED, locale);

        String content = processContent(templatePath, map);
        String subject = emailingSettings.getSubject(EmailType.NOT_SELECTIONNED, locale);

        sendEmail(user.getEmail(), subject, content, null);
    }

    @Async
    public void sendPending(User user, TalkUser talk, Locale locale) {
        log.debug("Sending not sectioned e-mail to '{}'", user.getEmail());

        HashMap<String, String> map = new HashMap<>();
        map.put("name", user.getFirstname());
        map.put("talk", talk.getName());
        map.put("hostname", globalSettings.getHostname());
        map.put("event", applicationConfigService.getAppConfig().getEventName());
        map.put("date", applicationConfigService.getAppConfig().getDate());

        String templatePath = emailingSettings.getTemplatePath(EmailType.PENDING, locale);

        String content = processContent(templatePath, map);
        String subject = emailingSettings.getSubject(EmailType.PENDING, locale);

        sendEmail(user.getEmail(), subject, content, null);
    }

    @Async
    public void sendSelectionned(User user, TalkUser talk, Locale locale) {
        log.debug("Sending not sectioned e-mail to '{}'", user.getEmail());

        HashMap<String, String> map = new HashMap<>();
        map.put("name", user.getFirstname());
        map.put("talk", talk.getName());
        map.put("hostname", globalSettings.getHostname());
        map.put("event", applicationConfigService.getAppConfig().getEventName());
        map.put("date", applicationConfigService.getAppConfig().getDate());
        map.put("releaseDate", applicationConfigService.getAppConfig().getReleaseDate());

        String templatePath = emailingSettings.getTemplatePath(EmailType.SELECTIONNED, locale);

        String content = processContent(templatePath, map);
        String subject = emailingSettings.getSubject(EmailType.SELECTIONNED, locale);

        sendEmail(user.getEmail(), subject, content, null);
    }
    /**
     * Send email validation.
     *
     * @param user
     * @param locale
     */
    @Async
    public void sendEmailValidation(User user, Locale locale) {
        log.debug("Sending email validation e-mail to '{}'", user.getEmail());

        String link = globalSettings.getHostname() + "/#/verify?id=" + String.valueOf(user.getId()) + "&token=" + user.getVerifyToken();
        log.debug(link);

        HashMap<String, String> map = new HashMap<>();
        map.put("link", link);
        map.put("hostname", globalSettings.getHostname());

        String templatePath = emailingSettings.getTemplatePath(EmailType.VERIFY, locale);

        String content = processContent(templatePath, map);
        String subject = emailingSettings.getSubject(EmailType.VERIFY, locale);

        sendEmail(user.getEmail(), subject, content, null);
    }



    public void sendEmail(String to, String subject, String content, List<String> bcc) {
        if (!emailingSettings.isSend())
            return;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(emailingSettings.getEmailSender());
            helper.setTo(to);
            if (bcc != null) {
                helper.setBcc(bcc.toArray(new String[bcc.size()]));
            }
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (MailSendException | MessagingException e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    public String processContent(String templatePath, Map<String, String> data) {
        // Init velocityEngine
        VelocityEngine velocityEngine = buildVelocityEngine();
        velocityEngine.init();
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, String> item : data.entrySet()) {
            context.put(item.getKey(), item.getValue());
        }

        Template template = velocityEngine.getTemplate(templatePath, "UTF-8");

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    private VelocityEngine buildVelocityEngine() {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityEngine(props);
    }
}
