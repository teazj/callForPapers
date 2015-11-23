package fr.sii.controller.restricted.user;

import fr.sii.domain.common.Key;
import fr.sii.domain.common.Uri;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by tmaugin on 09/07/2015.
 * SII
 */
@RestController
@RequestMapping(value="/api/restricted", produces = "application/json; charset=utf-8")
public class ProfilImagController {

    /**
     * Upload profil image to the Blobstore
     * @param req
     * @param res
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @RequestMapping(value="/upload", method= RequestMethod.POST)
    public Key upload(HttpServletRequest req, HttpServletResponse res) throws IOException, URISyntaxException {
        //TODO
        return new Key();
    }


}
