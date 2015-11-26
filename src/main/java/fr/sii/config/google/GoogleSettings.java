package fr.sii.config.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by tmaugin on 13/05/2015.
 */

/**
 * Google provider settings
 */
@Component
public class GoogleSettings {
    @Value("${cfp.google.clientid}")
    String clientId;
    @Value("${cfp.google.clientsecret}")
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
