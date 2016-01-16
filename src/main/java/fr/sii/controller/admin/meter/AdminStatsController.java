package fr.sii.controller.admin.meter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.sii.domain.admin.meter.AdminMeter;
import fr.sii.service.admin.stats.AdminStatsService;

@Controller
@RequestMapping(value="api/admin/stats", produces = "application/json; charset=utf-8")
public class AdminStatsController {

    @Autowired
    private AdminStatsService adminStatsService;

    /**
     * Get meter stats (talks count, draft count, ...)
     */
    @RequestMapping(value="/meter", method= RequestMethod.GET)
    @ResponseBody
    public AdminMeter getMeter() {
        return adminStatsService.getAdminMeter();
    }
}
