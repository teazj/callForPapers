package fr.sii.config.auth;

import fr.sii.service.auth.AuthUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by tmaugin on 19/05/2015.
 */

/**
 * Authentification settings
 */
@Component
public class AuthSettings {
    @Value("${cfp.auth.secrettoken}")
    String secretToken;

    @Value("${cfp.auth.captchasecret}")
    String captchaSecret;

    @Value("${cfp.auth.captchapublic}")
    String captchaPublic;

    @PostConstruct
    public void setToken() {
        AuthUtils.TOKEN_SECRET = secretToken;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public String getCaptchaSecret() {
        return captchaSecret;
    }

    public void setCaptchaSecret(String captchaSecret) {
        this.captchaSecret = captchaSecret;
    }

    public String getCaptchaPublic() {
        return captchaPublic;
    }

    public void setCaptchaPublic(String captchaPublic) {
        this.captchaPublic = captchaPublic;
    }
}
