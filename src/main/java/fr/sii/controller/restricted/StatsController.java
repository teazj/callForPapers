package fr.sii.controller.restricted;

import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.dto.RestrictedMeter;
import fr.sii.service.TalkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/restricted/stats")
public class StatsController extends RestrictedController {

    @Autowired
    private TalkUserService talkService;

    @RequestMapping("/meter")
    public RestrictedMeter meter(HttpServletRequest req) throws NotVerifiedException {
        int userId = retrieveUserId(req);

        RestrictedMeter res = new RestrictedMeter();
        res.setTalks(talkService.count(userId));
        return res;
    }

}
