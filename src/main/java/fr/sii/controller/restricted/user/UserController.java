package fr.sii.controller.restricted.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.domain.user.User;
import fr.sii.domain.user.UserProfile;
import fr.sii.service.auth.AuthUtils;
import fr.sii.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @RequestMapping(value="/user", method= RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserProfile(HttpServletRequest req) throws Exception {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }

        User u = userService.findById(Long.parseLong(claimsSet.getSubject()));
        ObjectMapper m = new ObjectMapper();
        UserProfile p = m.readValue(u.getProfile(), UserProfile.class);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("email", u.getEmail());
        map.put("name", p.getName());
        map.put("firstname", p.getFirstname());
        map.put("phone", p.getPhone());
        map.put("company", p.getCompany());
        map.put("bio", p.getBio());
        map.put("social", p.getSocial());

        return map;
    }

    @RequestMapping(value="/user", method= RequestMethod.PUT)
    @ResponseBody
    public UserProfile putUserProfile(HttpServletRequest req, @RequestBody UserProfile profile) throws Exception {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }

        User u = userService.findById(Long.parseLong(claimsSet.getSubject()));
        ObjectMapper m = new ObjectMapper();
        String profileString = m.writeValueAsString(profile);
        u.setProfile(profileString);
        userService.put(u.getEntityId(),u);
        return profile;
    }
}
