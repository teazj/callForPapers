package fr.sii.config.google;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by tmaugin on 13/05/2015.
 */

/**
 * Google provider settings
 */
public class GoogleSettings {
    @Value("${google.clientid}")
    String clientId;
    @Value("${google.clientsecret}")
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