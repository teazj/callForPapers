package fr.sii.controller.restricted.meter;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import fr.sii.domain.restricted.meter.RestrictedMeter;
import fr.sii.service.restricted.stats.RestrictedStatsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by tmaugin on 24/08/2015.
 * SII
 */
@Controller
@RequestMapping(value="api/restricted/stats", produces = "application/json; charset=utf-8")
public class RestrictedStatsController {

    private RestrictedStatsService restrictedStatsService;

    public void setRestrictedStatsService(RestrictedStatsService restrictedStatsService) {
        this.restrictedStatsService = restrictedStatsService;
    }

    /**
     * Get meter stats (talks count)
     * @return
     * @throws IOException
     * @throws ServiceException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/meter", method= RequestMethod.GET)
    @ResponseBody
    public RestrictedMeter getMeter() throws IOException, ServiceException, EntityNotFoundException {
        return restrictedStatsService.getRestrictedMeter();
    }
}