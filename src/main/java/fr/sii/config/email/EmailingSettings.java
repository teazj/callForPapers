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
    @Value("${email.smtphost}")
    private String smtphost;
    @Value("${email.smtpport}")
    private String smtpport;
    @Value("${email.password}")
    private String password;
    @Value("${email.username}")
    private String username;
    @Value("${email.send}")
    private boolean send;

    public String getSmtphost() {
        return smtphost;
    }

    public void setSmtphost(String smtphost) {
        this.smtphost = smtphost;
    }

    public String getSmtpport() {
        return smtpport;
    }

    public void setSmtpport(String smtpport) {
        this.smtpport = smtpport;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }
}