package fr.sii.service.admin.config;

import fr.sii.dto.ApplicationSettings;
import fr.sii.entity.CfpConfig;
import fr.sii.repository.CfpConfigRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lhuet on 21/11/15.
 */
@Service
public class ApplicationConfigService {

    private static final String COMMUNITY = "community";
    private static final String EVENT_NAME = "eventName";
    private static final String DATE = "date";
    private static final String DECISION_DATE = "decisionDate";
    private static final String RELEASE_DATE = "releaseDate";
    private static final String OPEN = "open";
    private final Logger log = LoggerFactory.getLogger(ApplicationConfigService.class);
    @Autowired
    private CfpConfigRepo cfpConfigRepo;

    @Cacheable("applicationSettings")
    public ApplicationSettings getAppConfig() {
        ApplicationSettings applicationSettings = new ApplicationSettings();

        applicationSettings.setEventName(cfpConfigRepo.findValueByKey(EVENT_NAME));
        applicationSettings.setCommunity(cfpConfigRepo.findValueByKey(COMMUNITY));
        applicationSettings.setDate(cfpConfigRepo.findValueByKey(DATE));
        applicationSettings.setDecisionDate(cfpConfigRepo.findValueByKey(DECISION_DATE));
        applicationSettings.setReleaseDate(cfpConfigRepo.findValueByKey(RELEASE_DATE));
        applicationSettings.setOpen(Boolean.valueOf(cfpConfigRepo.findValueByKey(OPEN)));
        applicationSettings.setConfigured(true);

        return applicationSettings;
    }

    @Cacheable("isCfpOpen")
    public boolean isCfpOpen() {
        CfpConfig conf = cfpConfigRepo.findByKey(OPEN);
        return Boolean.valueOf(conf.getValue());
    }

    @Transactional
    @CacheEvict("isCfpOpen")
    public void openCfp() {
        saveConf(OPEN, "true");
    }

    @Transactional
    @CacheEvict("isCfpOpen")
    public void closeCfp() {
        saveConf(OPEN, "false");
    }

    @CacheEvict("applicationSettings")
    @Transactional
    public void saveConf(String key, String value) {
        CfpConfig conf = cfpConfigRepo.findByKey(key);
        conf.setValue(value);
        cfpConfigRepo.save(conf);

        log.debug(conf.getKey() + " -> " + conf.getValue());
    }

    @CacheEvict("applicationSettings")
    @Transactional
    public void saveConfiguration(ApplicationSettings settings) {
        saveConf(COMMUNITY, settings.getCommunity());
        saveConf(EVENT_NAME, settings.getEventName());
        saveConf(DATE, settings.getDate());
        saveConf(DECISION_DATE, settings.getDecisionDate());
        saveConf(RELEASE_DATE, settings.getReleaseDate());
        saveConf(OPEN, String.valueOf(settings.isOpen()));
    }
}
