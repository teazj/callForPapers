package fr.sii.config.global;

/**
 * Created by tmaugin on 02/04/2015.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Application settings (dev, prod, test)
 */
@Component
public class GlobalSettings {
    @Value("${database.loaded}")
    private String databaseLoaded;

    @Value("${app.hostname}")
    private String hostname;

    public String getDatabaseLoaded() {
        return databaseLoaded;
    }

    public void setDatabaseLoaded(String databaseLoaded) {
        this.databaseLoaded = databaseLoaded;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}