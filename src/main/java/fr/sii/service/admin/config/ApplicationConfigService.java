package fr.sii.service.admin.config;

import fr.sii.dto.ApplicationSettings;
import fr.sii.entity.CfpConfig;
import fr.sii.repository.CfpConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lhuet on 21/11/15.
 */
@Service
@Transactional
public class ApplicationConfigService {

    @Autowired
    private CfpConfigRepo cfpConfigRepo;

    public ApplicationSettings getAppConfig() {
        ApplicationSettings applicationSettings = new ApplicationSettings();

        applicationSettings.setEventName(cfpConfigRepo.findValueByKey("eventName"));
        applicationSettings.setCommunity(cfpConfigRepo.findValueByKey("community"));
        applicationSettings.setDate(cfpConfigRepo.findValueByKey("date"));
        applicationSettings.setDecisionDate(cfpConfigRepo.findValueByKey("decisionDate"));
        applicationSettings.setReleaseDate(cfpConfigRepo.findValueByKey("releaseDate"));
        applicationSettings.setOpen(Boolean.valueOf(cfpConfigRepo.findValueByKey("open")));
        applicationSettings.setConfigured(true);

        return applicationSettings;
    }


    public boolean isCfpOpen() {
        CfpConfig conf = cfpConfigRepo.findByKey("open");
        return Boolean.valueOf(conf.getValue());
    }

    public void openCfp() {
        saveConf("open", "true");
    }

    public void closeCfp() {
        saveConf("open", "false");
    }

    public void saveConf(String key,String value){
        CfpConfig conf = cfpConfigRepo.findByKey(key);
        conf.setValue(value);
        cfpConfigRepo.save(conf);
        System.out.println(conf.getKey() + " -> " + conf.getValue());
    }

    public void saveConfiguration(ApplicationSettings settings) {
        saveConf("community", settings.getCommunity());
        saveConf("eventName", settings.getEventName());
        saveConf("date", settings.getDate());
        saveConf("decisionDate", settings.getDecisionDate());
        saveConf("releaseDate", settings.getReleaseDate());
        saveConf("open", String.valueOf(settings.isOpen()));
    }
}
