package fr.sii.config.application;

/**
 * Created by tmaugin on 30/04/2015.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.appengine.api.datastore.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Public application settings
 */
@Component
public class ApplicationSettings {
    @Value("${app.eventName}")
    private String eventName;

    @Value("${app.community}")
    private String community;

    @Value("${app.date}")
    private String date;

    @Value("${app.releasedate}")
    private String releaseDate;

    @Value("${app.decisiondate}")
    private String decisionDate;

    private boolean configured = true;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(String decisionDate) {
        this.decisionDate = decisionDate;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    @JsonProperty
    public boolean getOpen() {
        Key applicationConfigKey = KeyFactory.createKey("Config", "Application");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        boolean data = false;
        try {
            Entity refreshToken = datastore.get(applicationConfigKey);
            data = (boolean) refreshToken.getProperty("enableSubmissions");
        } catch (EntityNotFoundException e) {
            return false;
        }
        return data;
    }
}