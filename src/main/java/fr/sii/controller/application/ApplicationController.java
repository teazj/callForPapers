package fr.sii.controller.application;

import fr.sii.config.application.ApplicationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Created by tmaugin on 07/05/2015.
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