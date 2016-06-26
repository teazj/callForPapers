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

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import io.cfp.dto.TalkAdmin;
import io.cfp.dto.TalkUser;
import io.cfp.dto.user.CospeakerProfil;
import io.cfp.dto.user.UserProfil;
import io.cfp.entity.Event;
import io.cfp.entity.User;
import io.cfp.repository.EventRepository;
import io.cfp.repository.UserRepo;
import io.cfp.service.admin.config.ApplicationConfigService;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class EmailingService {

    private final Logger log = LoggerFactory.getLogger(EmailingService.class);

    @Autowired
    private ApplicationConfigService applicationConfigService;

    @Autowired
    private UserRepo users;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${cfp.email.sendgrid.apikey}")
    private String sendgridApiKey;

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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional
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

        createAndSendEmail("notSelectionned.html", user.getEmail(), params, cc, null, locale);
    }

    @Async
    @Transactional
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

        createAndSendEmail("pending.html", user.getEmail(), params, cc, null, locale);
    }

    @Async
    @Transactional
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

        createAndSendEmail("selectionned.html", user.getEmail(), params, cc, null, locale);
    }

    protected void createAndSendEmail(String template, String email, Map<String,Object> parameters, List<String> cc, List<String> bcc, Locale locale) {
        String templatePath = getTemplatePath(template, locale);

        String content = processTemplate(templatePath, parameters);
        String subject = (String) parameters.get("subject");

        sendEmail(parameters.get("contactMail").toString(), email, subject, content, cc, bcc);
    }

    protected String getTemplatePath(final String emailTemplate, final Locale locale) {
    	String language = locale.getLanguage();
    	if (!"fr".equals(language)) {
    		language = "en";
    	}
        return "mails/" + language + "/" + emailTemplate;
    }

    protected String processTemplate(String templatePath, Map<String, Object> parameters) {

        // adds global params
        parameters.put("hostname", StringUtils.replace(hostname, "{{event}}", Event.current()));
        parameters.put("date", new DateTool());
        Event curEvent = eventRepo.findOne(Event.current());
        parameters.put("event", curEvent);
        parameters.put("contactMail", curEvent.getContactMail() != null ? curEvent.getContactMail() : "contact@cfp.io");

        VelocityContext context = new VelocityContext(parameters);

        Template template = velocityEngine.getTemplate(templatePath, "UTF-8");

        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    public void sendEmail(String from, String to, String subject, String content, List<String> cc, List<String> bcc) {
        if (!send) {
            String fileName = saveLocally(content);
            log.warn("Mail [{}] to [{}] not sent as mail is disabled but can be found at [{}]", subject, to, fileName);
            return;
        }

        SendGrid sendgrid = new SendGrid(sendgridApiKey);

        SendGrid.Email email = new SendGrid.Email();

        email.setFrom(emailSender)
            .setFromName("CFP.io")
            .setReplyTo(from)
            .addTo(to)
            .setSubject(subject)
            .setHtml(content);
        if (cc != null) {
            email.addCc(cc.toArray(new String[cc.size()]));
        }
        if (bcc != null) {
            email.addBcc(bcc.toArray(new String[bcc.size()]));
        }


        try {
            SendGrid.Response response = sendgrid.send(email);
            log.debug("Sent e-mail to User '{}' with status {}", to, response.getStatus());
        } catch (SendGridException e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }



    private String saveLocally(String content) {
        try {
            File tempFile = File.createTempFile("cfpio-", ".html");
            FileUtils.writeStringToFile(tempFile, content, StandardCharsets.UTF_8);
            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            log.error("Unable to save temp mail file", e);
            return null;
        }
    }
}
