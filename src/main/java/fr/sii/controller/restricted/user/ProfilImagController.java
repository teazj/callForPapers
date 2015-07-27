package fr.sii.controller.restricted.user;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import fr.sii.domain.common.Key;
import fr.sii.domain.common.Uri;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by tmaugin on 09/07/2015.
 * SII
 */
@RequestMapping(value="/api/restricted", produces = "application/json; charset=utf-8")
public class ProfilImagController {

    private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    /**
     * Upload profil image to the Blobstore
     * @param req
     * @param res
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @RequestMapping(value="/upload", method= RequestMethod.POST)
    @ResponseBody
    public Key upload(HttpServletRequest req, HttpServletResponse res) throws IOException, URISyntaxException {
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("file");

        if (blobKeys != null && !blobKeys.isEmpty()) {
            return new Key( blobKeys.get(0).getKeyString());
        }
        return new Key();
    }

    /**
     * Obtain Blobstore upload URL
     * @param req
     * @param res
     * @return
     */
    @RequestMapping(value="/upload", method= RequestMethod.GET)
    @ResponseBody
    public Uri getUploadUrl(HttpServletRequest req, HttpServletResponse res) {
        return new Uri(blobstoreService.createUploadUrl("/api/restricted/upload"));
    }
}
