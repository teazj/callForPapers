package fr.sii.controller.application;

import fr.sii.config.application.ApplicationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by tmaugin on 30/04/2015.
 */
@Controller
@RequestMapping(value="/application")
public class ApplicationController {

    @Autowired
    ApplicationSettings applicationSettings;

    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }
}