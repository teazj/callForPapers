/*
 * Copyright (c) 2016 BreizhCamp
 * [http://breizhcamp.org]
 *
 * This file is part of CFP.io.
 *
 * CFP.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.cfp.service.email;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.cfp.dto.TalkAdmin;
import io.cfp.dto.TalkUser;
import io.cfp.dto.user.CospeakerProfil;
import io.cfp.dto.user.UserProfil;
import io.cfp.entity.Event;
import io.cfp.entity.User;
import io.cfp.repository.UserRepo;
import io.cfp.service.admin.config.ApplicationConfigService;

@Service
public class EmailingService {

    private final Logger log = LoggerFactory.getLogger(EmailingService.class);
    
    @Autowired
    private ApplicationConfigService applicationConfigService;
    
    @Autowired
    private UserRepo users;
    
    @Autowired
    private VelocityEngine velocityEngine;
    
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    
    @Value("${cfp.app.hostname}")
    private String hostname;
    
    @Value("${cfp.email.emailsender}")
    private String emailSender;

    @Value("${cfp.email.send}")
    private boolean send;

   
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

        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getFirstname());
        params.put("talk", talk.getName());
        params.put("id", String.valueOf(talk.getId()));
       
        createAndSendEmail("confirmed.html", user.getEmail(), params, null, null, locale);
    }

    /**
     * Send an email to a speaker to notify him that an administrator wrote a
     * new comment about his talk.
     *
     * @param speaker
     *            the speaker to write to
     * @param talk
     *            talk under review
     * @param locale
     */
    @Async
    public void sendNewCommentToSpeaker(User speaker, TalkAdmin talk, Locale locale) {
        log.debug("Sending new comment email to speaker '{}' for talk '{}'", speaker.getEmail(), talk.getName());

        List<String> cc = users.findAdminsEmail(Event.current());

        Map<String, Object> params = new HashMap<>();
        params.put("name", speaker.getFirstname());
        params.put("talk", talk.getName());
        params.put("id", String.valueOf(talk.getId()));
       
        createAndSendEmail("newMessage.html", speaker.getEmail(), params, cc, null, locale);
    }

    /**
     * Send an email to administrators to notify them that a speaker wrote a
     * new comment on his talk.
     *
     * @param speaker
     *            the speaker writing this message
     * @param talk
     *            talk under review
     * @param locale
     */
    @Async
    public void sendNewCommentToAdmins(User speaker, TalkUser talk, Locale locale) {
        log.debug("Sending new comment email to admins for talk '{}'", talk.getName());

        List<String> bcc = users.findAdminsEmail(Event.current());

        Map<String, Object> params = new HashMap<>();
        params.put("name", speaker.getFirstname());
        params.put("talk", talk.getName());
        params.put("id", String.valueOf(talk.getId()));

        createAndSendEmail("newMessageAdmin.html", emailSender, params, null, bcc, locale);
    }

    /**
     * Send Confirmation of selection.
     *
     * @param talk
     * @param locale
     */
    @Async
    public void sendNotSelectionned(TalkUser talk, Locale locale) {
        UserProfil user = talk.getSpeaker();

        log.debug("Sending not selectionned e-mail to '{}'", user.getEmail());

        List<String> cc = new ArrayList<>();
        if (talk.getCospeakers() != null) {
            for (CospeakerProfil cospeakerProfil : talk.getCospeakers()) {
                cc.add(cospeakerProfil.getEmail());
            }
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getFirstname());
        params.put("talk", talk.getName());
        params.put("shortDescription", applicationConfigService.getAppConfig().getShortDescription());
        params.put("event", applicationConfigService.getAppConfig().getEventName());

        createAndSendEmail("notSelectionned.html", user.getEmail(), params, cc, null, locale);
    }

    @Async
    public void sendPending(TalkUser talk, Locale locale) {
        UserProfil user = talk.getSpeaker();

        log.debug("Sending pending e-mail to '{}'", user.getEmail());

        List<String> cc = new ArrayList<>();
        if (talk.getCospeakers() != null) {
            for (CospeakerProfil cospeakerProfil : talk.getCospeakers()) {
                cc.add(cospeakerProfil.getEmail());
            }
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getFirstname());
        params.put("talk", talk.getName());
        params.put("event", applicationConfigService.getAppConfig().getEventName());
        params.put("date", applicationConfigService.getAppConfig().getDate());

        createAndSendEmail("pending.html", user.getEmail(), params, cc, null, locale);
    }

    @Async
    public void sendSelectionned(TalkUser talk, Locale locale) {
        UserProfil user = talk.getSpeaker();

        log.debug("Sending selectionned e-mail to '{}'", user.getEmail());

        List<String> cc = new ArrayList<>();
        if (talk.getCospeakers() != null) {
            for (CospeakerProfil cospeakerProfil : talk.getCospeakers()) {
                cc.add(cospeakerProfil.getEmail());
            }
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getFirstname());
        params.put("talk", talk.getName());
        params.put("event", applicationConfigService.getAppConfig().getEventName());
        params.put("date", applicationConfigService.getAppConfig().getDate());
        params.put("releaseDate", applicationConfigService.getAppConfig().getReleaseDate());

        createAndSendEmail("selectionned.html", user.getEmail(), params, cc, null, locale);
    }
    
    public void createAndSendEmail(String template, String email, Map<String,Object> parameters, List<String> cc, List<String> bcc, Locale locale) {
    	String templatePath = getTemplatePath(template, locale);

        String content = processTemplate(templatePath, parameters);
        String subject = (String) parameters.get("subject");

        sendEmail(email, subject, content, cc, bcc);
    }

    public String getTemplatePath(final String emailTemplate, final Locale locale) {
    	String language = locale.getLanguage();
    	if (!"fr".equals(language)) {
    		language = "en";
    	}
        return "mails/" + language + "/" + emailTemplate;
    }
    
    public String processTemplate(String templatePath, Map<String, Object> parameters) {
        
        // adds global params
        parameters.put("hostname", StringUtils.replace(hostname, "{{event}}", Event.current()));
        
        VelocityContext context = new VelocityContext(parameters);

        Template template = velocityEngine.getTemplate(templatePath, "UTF-8");

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    public void sendEmail(String to, String subject, String content, List<String> cc, List<String> bcc) {
        if (!send)
            return;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(emailSender);
            helper.setTo(to);
            if (bcc != null) {
                helper.setBcc(bcc.toArray(new String[bcc.size()]));
            }
            if (cc != null) {
                helper.setCc(cc.toArray(new String[cc.size()]));
            }
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (MailSendException | MessagingException e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

}
