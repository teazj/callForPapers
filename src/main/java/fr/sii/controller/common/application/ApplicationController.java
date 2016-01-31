package fr.sii.controller.common.application;

import fr.sii.dto.ApplicationSettings;
import fr.sii.service.admin.config.ApplicationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tmaugin on 07/05/2015.
 */
@RestController
@RequestMapping(value="/api", produces = "application/json; charset=utf-8")
public class ApplicationController {

    @Autowired
    private ApplicationConfigService applicationConfigService;

    /**
     * Obtain application settings, (name, dates, ...)
     * @return
     */
    @RequestMapping(method=RequestMethod.GET, value="/application")
    public ApplicationSettings getApplicationSettings() {

        return applicationConfigService.getAppConfig();
    }


    /**
     * save application settings, (name, dates, ...)
     * @return
     */
    @RequestMapping(method=RequestMethod.POST, value="/admin/application")
    public void getApplicationSettings(@RequestBody ApplicationSettings settings) {
        applicationConfigService.saveConfiguration(settings);
    }
}
