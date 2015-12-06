package fr.sii.controller.common;

import fr.sii.config.global.ServiceProviderSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/settings", produces = "application/json; charset=utf-8")
public class SettingsController {

    @Autowired
    private ServiceProviderSettings serviceProviderSettings;

    @RequestMapping(value="/serviceproviders", method= RequestMethod.GET)
    public ServiceProviderSettings getServiceProviderSettings() {
        return serviceProviderSettings;
    }
}
