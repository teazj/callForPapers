package fr.sii.controller.common.user;

import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.common.Uri;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.entity.User;
import fr.sii.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value="/api/profil")
public class CommonProfilImageController {

    @Autowired
    private UserService userService;

    @Autowired
    private GlobalSettings globalSettings;

    /**
     * Get user profil image key
     * @param req
     * @param res
     * @param id
     * @throws IOException
     * @throws NotFoundException
     */
    @RequestMapping(value="/image/user/{id}", method= RequestMethod.GET)
    public void getProfileImageByUserId(HttpServletRequest req, HttpServletResponse res,  @PathVariable Integer id) throws IOException, NotFoundException {
        User u = userService.findById(id);
        if (u == null) throw new NotFoundException("User not found");

        byte[] image = u.getImage();
        if (image.length == 0) {
            throw new NotFoundException("Image not found");
        }

        res.getOutputStream().write(image);
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
    public Uri getProfilImageUrlByUserId(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer id) throws IOException, NotFoundException {
        User u = userService.findById(id);
        if (u == null) throw new NotFoundException("User not found");

        byte[] image = u.getImage();

        if(image.length > 0) {
            return new Uri(globalSettings.getHostname() + "/api/profil/image/user/" + id);
        }

        if(u.getImageSocialUrl() != null && u.getImageSocialUrl().length() > 0) {
            return new Uri(u.getImageSocialUrl());
        }
        return new Uri();
    }
}
