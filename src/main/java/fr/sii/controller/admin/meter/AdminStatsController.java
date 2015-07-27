package fr.sii.controller.admin.meter;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import fr.sii.domain.admin.meter.AdminMeter;
import fr.sii.service.admin.stats.AdminStatsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by tmaugin on 16/07/2015.
 * SII
 */
@Controller
@RequestMapping(value="api/admin/stats", produces = "application/json; charset=utf-8")
public class AdminStatsController {

    private AdminStatsService adminStatsService;

    public void setAdminStatsService(AdminStatsService adminStatsService) {
        this.adminStatsService = adminStatsService;
    }

    /**
     * Get meter stats (talks count, draft count, ...)
     * @return
     * @throws IOException
     * @throws ServiceException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/meter", method= RequestMethod.GET)
    @ResponseBody
    public AdminMeter getMeter() throws IOException, ServiceException, EntityNotFoundException {
        return adminStatsService.getAdminMeter();
    }
}