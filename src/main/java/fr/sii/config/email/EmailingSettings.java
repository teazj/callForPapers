package fr.sii.config.email;

/**
 * Created by tmaugin on 02/04/2015.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Email settings
 */
@Component
public class EmailingSettings {
    @Value("${cfp.email.emailsender}")
    private String emailSender;
    @Value("${cfp.email.send}")
    private boolean send;

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }
}
