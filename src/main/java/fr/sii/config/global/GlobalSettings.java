package fr.sii.config.global;

/**
 * Created by tmaugin on 02/04/2015.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalSettings {
    @Value("${database.loaded}")
    private String databaseLoaded;

    public String getDatabaseLoaded() {
        return databaseLoaded;
    }

    public void setDatabaseLoaded(String databaseLoaded) {
        this.databaseLoaded = databaseLoaded;
    }
}