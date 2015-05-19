package fr.sii.config.auth;

import fr.sii.service.auth.AuthUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by tmaugin on 19/05/2015.
 */
@Configuration
public class AuthConfig {
    @Value("${auth.secrettoken}")
    String secretToken;

    @PostConstruct
    public void setToken()
    {
        AuthUtils.TOKEN_SECRET = secretToken;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }
}
