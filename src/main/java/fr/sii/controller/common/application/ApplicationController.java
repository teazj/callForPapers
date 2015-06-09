package fr.sii.controller.common.application;

import fr.sii.config.application.ApplicationSettings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by tmaugin on 07/05/2015.
 */
@Controller
@RequestMapping(value="/api", produces = "application/json; charset=utf-8")
public class ApplicationController {

    ApplicationSettings applicationSettings;

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    @RequestMapping(value="/application", method= RequestMethod.GET)
    @ResponseBody
    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }
}