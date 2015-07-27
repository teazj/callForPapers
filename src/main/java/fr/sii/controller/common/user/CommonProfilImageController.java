package fr.sii.controller.common.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.common.Uri;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.user.User;
import fr.sii.domain.user.UserProfil;
import fr.sii.service.user.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tmaugin on 09/07/2015.
 * SII
 */

@RequestMapping(value="/api/profil")
public class CommonProfilImageController {

    private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private GlobalSettings globalSettings;

    public void setGlobalSettings(GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    /**
     * Get image for given BlobKey
     * @param req
     * @param res
     * @param blobKeyParam
     * @throws IOException
     */
    @RequestMapping(value="/image/{blobKeyParam}", method= RequestMethod.GET)
    @ResponseBody
    public void getProfileImage(HttpServletRequest req, HttpServletResponse res,  @PathVariable String blobKeyParam) throws IOException {
        BlobKey blobKey = new BlobKey(blobKeyParam);
        blobstoreService.serve(blobKey, res);
    }

    /**
     * Get user profil image key
     * @param req
     * @param res
     * @param id
     * @throws IOException
     * @throws NotFoundException
     */
    @RequestMapping(value="/image/user/{id}", method= RequestMethod.GET)
    @ResponseBody
    public void getProfileImageByUserId(HttpServletRequest req, HttpServletResponse res,  @PathVariable Long id) throws IOException, NotFoundException {
        User u = userService.findById(id);
        if(u == null)
        {
            throw new NotFoundException("User not found");
        }

        ObjectMapper m = new ObjectMapper();
        String userProfile = u.getProfile();
        userProfile = (userProfile == null) ? "{}" : userProfile;
        UserProfil p = m.readValue(userProfile, UserProfil.class);

        if(p.getImageProfilKey() != null) {
            BlobKey blobKey = new BlobKey((p.getImageProfilKey()));
            blobstoreService.serve(blobKey, res);
        } else {
            throw new NotFoundException("Image not found");
        }
    }

    /**
     * Get user profil image URL
     * @param req
     * @param res
     * @param id
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    @RequestMapping(value="/image/user/url/{id}", method= RequestMethod.GET)
    @ResponseBody
    public Uri getProfilImageUrlByUserId(HttpServletRequest req, HttpServletResponse res,  @PathVariable Long id) throws IOException, NotFoundException {
        User u = userService.findById(id);
        if(u == null)
        {
            throw new NotFoundException("User not found");
        }

        ObjectMapper m = new ObjectMapper();
        String userProfile = u.getProfile();
        userProfile = (userProfile == null) ? "{}" : userProfile;
        UserProfil p = m.readValue(userProfile, UserProfil.class);

        if(p.getImageProfilKey() != null) {
            return new Uri(globalSettings.getHostname() + "/api/profil/image/user/" + id);
        }

        if(p.getSocialProfilImageUrl() != null && !p.getSocialProfilImageUrl().equals("")) {
            return new Uri(p.getSocialProfilImageUrl());
        }
        return new Uri();
    }
}
