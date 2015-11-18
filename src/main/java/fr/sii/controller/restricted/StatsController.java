package fr.sii.controller.restricted;

import com.nimbusds.jwt.JWTClaimsSet;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.dto.RestrictedMeter;
import fr.sii.service.TalkUserService;
import fr.sii.service.auth.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/restricted/stats")
public class StatsController {

    @Autowired
    private TalkUserService talkService;

    @RequestMapping("/meter")
    public RestrictedMeter meter(HttpServletRequest req) throws NotVerifiedException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified")) {
            throw new NotVerifiedException("User must be verified");
        }
        int userId = Integer.parseInt(claimsSet.getSubject());

        RestrictedMeter res = new RestrictedMeter();
        res.setTalks(talkService.count(userId));
        return res;
    }

}
