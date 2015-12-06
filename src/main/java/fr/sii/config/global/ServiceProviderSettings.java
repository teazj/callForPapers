package fr.sii.config.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderSettings {
    @Value("${cfp.github.clientid}")
    String githubClientId;

    @Value("${cfp.google.clientid}")
    String googleClientId;

    @Value("${cfp.auth.captchapublic}")
    String recaptchaPublicKey;

    public String getGoogleClientId() {
        return googleClientId;
    }

    public String getRecaptchaPublicKey() {
        return recaptchaPublicKey;
    }

    public String getGithubClientId() {
        return githubClientId;
    }
}
