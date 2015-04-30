package fr.sii.config.application;

/**
 * Created by tmaugin on 30/04/2015.
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSettings {
    @Value("${app.eventName}")
    private String eventName;

    @Value("${app.community}")
    private String community;

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
}