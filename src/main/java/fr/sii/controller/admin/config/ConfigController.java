package fr.sii.controller.admin.config;

import fr.sii.service.admin.config.ApplicationConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Created by tmaugin on 16/07/2015.
 * SII
 */
@RestController
@RequestMapping(value="api/admin/config", produces = "application/json; charset=utf-8")
public class ConfigController {

    @Autowired
    private ApplicationConfigService applicationConfigService;

    /**
     * Disable or enable submission of new talks
     * @param key enable submission if true, else disable
     * @return key
     */
    @RequestMapping(value="/enableSubmissions", method= RequestMethod.POST)
    public fr.sii.domain.common.Key postEnableSubmissions(@Valid @RequestBody fr.sii.domain.common.Key key) {

        if (key.getKey().equals("true"))
            applicationConfigService.openCfp();
        if (key.getKey().equals("false"))
            applicationConfigService.closeCfp();

        return key;
    }
}