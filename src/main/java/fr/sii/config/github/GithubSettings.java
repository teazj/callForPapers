package fr.sii.config.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by tmaugin on 13/05/2015.
 */
@Component
public class GithubSettings {
    @Value("${github.clientid}")
    String clientId;
    @Value("${github.clientsecret}")
    String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}