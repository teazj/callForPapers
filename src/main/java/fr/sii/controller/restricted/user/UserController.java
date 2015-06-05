package fr.sii.controller.restricted.user;

import com.nimbusds.jwt.JWTClaimsSet;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.domain.user.User;
import fr.sii.service.auth.AuthUtils;
import fr.sii.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tmaugin on 05/06/2015.
 * SII
 */
@Controller
@RequestMapping(value="/api/restricted", produces = "application/json; charset=utf-8")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * SESSION
     */
    @RequestMapping(value="/user", method= RequestMethod.GET)
    @ResponseBody
    public Map<String, String> postGoogleSpreadsheet(HttpServletRequest req) throws Exception {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }

        User u = userService.findById(Long.parseLong(claimsSet.getSubject()));

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("email", u.getEmail());

        return map;
    }
}
