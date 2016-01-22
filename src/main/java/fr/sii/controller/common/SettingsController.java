package fr.sii.controller.common;

import fr.sii.config.global.ServiceProviderSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.sii.entity.Talk;
import fr.sii.entity.TalkFormat;
import java.util.List;
import fr.sii.domain.exception.NotVerifiedException;

import fr.sii.service.TalkUserService;
import fr.sii.dto.TrackDto;

@RestController
@RequestMapping(value="/api/settings", produces = "application/json; charset=utf-8")
public class SettingsController {

    @Autowired
    private ServiceProviderSettings serviceProviderSettings;

    @Autowired
    private TalkUserService talkService;

    @RequestMapping(value="/serviceproviders", method= RequestMethod.GET)
    public ServiceProviderSettings getServiceProviderSettings() {
        return serviceProviderSettings;
    }

    /**
     * Obtain list of talk formats
     */
    @RequestMapping(value = "/talk/formats")
    public List<TalkFormat> getTalkFormat() {
        return talkService.getTalkFormat();
    }

    /**
     * Get all session for the current user
     */
    @RequestMapping(value = "/talk/tracks", method = RequestMethod.GET)
    public List<TrackDto> getTrack() throws NotVerifiedException {
        return talkService.getTracks();
    }
}
