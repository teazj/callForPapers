package fr.sii.controller.restricted.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gdata.util.ServiceException;
import com.nimbusds.jwt.JWTClaimsSet;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.entity.User;
import fr.sii.dto.user.UserProfil;
import fr.sii.service.auth.AuthUtils;
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

@Controller
@RequestMapping(value="/api/restricted", produces = "application/json; charset=utf-8")
public class UserController {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
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

        User u = userService.findById(Integer.parseInt(claimsSet.getSubject()));
        if(u == null)
        {
            throw new NotFoundException("User not found");
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("email", u.getEmail());
        map.put("name", u.getLastname());
        map.put("firstname", u.getFirstname());
        map.put("phone", u.getPhone());
        map.put("company", u.getCompany());
        map.put("bio", u.getBio());
        map.put("social", u.getSocial());
        map.put("twitter", u.getTwitter());
        map.put("googlePlus", u.getGoogleplus());
        map.put("github", u.getGithub());
        map.put("imageProfilKey", u.getImageProfilKey());
        map.put("socialProfilImageUrl", u.getImageSocialUrl());

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

        User u = userService.findById(Integer.parseInt(claimsSet.getSubject()));
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
            Queue queue = QueueFactory.getQueue("profil");
            // Add profil update to the queue
            TaskOptions options = TaskOptions.Builder.withUrl("/worker/profil").param("profil", profilString).param("userId", claimsSet.getSubject());
            queue.add(options);
        }
        return profil;
    }
}
