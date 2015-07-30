package fr.sii.controller.restricted.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import com.nimbusds.jwt.JWTClaimsSet;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.domain.user.User;
import fr.sii.domain.user.UserProfil;
import fr.sii.service.auth.AuthUtils;
import fr.sii.service.spreadsheet.SpreadsheetService;
import fr.sii.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tmaugin on 05/06/2015.
 * SII
 */
@Controller
@RequestMapping(value="/api/restricted", produces = "application/json; charset=utf-8")
public class UserController {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private SpreadsheetService googleService;

    public void setGoogleService(SpreadsheetService googleService) {
        this.googleService = googleService;
    }

    /**
     * Get current user profil
     * @param req
     * @return
     * @throws NotVerifiedException
     * @throws NotFoundException
     * @throws IOException
     */
    @RequestMapping(value="/user", method= RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getUserProfil(HttpServletRequest req) throws NotVerifiedException, NotFoundException, IOException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }

        User u = userService.findById(Long.parseLong(claimsSet.getSubject()));
        if(u == null)
        {
            throw new NotFoundException("User not found");
        }

        ObjectMapper m = new ObjectMapper();
        String userProfile = u.getProfile();
        userProfile = (userProfile == null) ? "{}" : userProfile;
        UserProfil p = m.readValue(userProfile, UserProfil.class);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("email", u.getEmail());
        map.put("name", p.getName());
        map.put("firstname", p.getFirstname());
        map.put("phone", p.getPhone());
        map.put("company", p.getCompany());
        map.put("bio", p.getBio());
        map.put("social", p.getSocial());
        map.put("twitter", p.getTwitter());
        map.put("googlePlus", p.getGooglePlus());
        map.put("github", p.getGithub());
        map.put("imageProfilKey", p.getImageProfilKey());
        map.put("socialProfilImageUrl", p.getSocialProfilImageUrl());

        return map;
    }

    /**
     * Edit current user profil
     * @param req
     * @param profil
     * @return
     * @throws NotVerifiedException
     * @throws NotFoundException
     * @throws IOException
     * @throws EntityNotFoundException
     * @throws ServiceException
     */
    @RequestMapping(value="/user", method= RequestMethod.PUT)
    @ResponseBody
    public UserProfil putUserProfil(HttpServletRequest req, @RequestBody UserProfil profil) throws NotVerifiedException, NotFoundException, IOException, EntityNotFoundException, ServiceException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }

        User u = userService.findById(Long.parseLong(claimsSet.getSubject()));
        if(u == null)
        {
            throw new NotFoundException("User not found");
        }

        // remove old image if different from posted
        try {
            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            ObjectMapper m = new ObjectMapper();
            String userProfil = u.getProfile();
            userProfil = (userProfil == null) ? "{}" : userProfil;
            UserProfil p = m.readValue(userProfil, UserProfil.class);
            if(p.getImageProfilKey() != null && !p.getImageProfilKey().equals(profil.getImageProfilKey())) {
                blobstoreService.delete(new BlobKey(p.getImageProfilKey()));
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }

        ObjectMapper m = new ObjectMapper();
        String profilString = m.writeValueAsString(profil);
        if(!u.getProfile().equals(profilString)) {
            u.setProfile(profilString);
            userService.put(u.getEntityId(), u);
            googleService.updateProfilSessions(profil, Long.parseLong(claimsSet.getSubject()));
        }
        return profil;
    }
}
