package fr.sii.controller.restricted.session;

/**
 * Created by tmaugin on 15/05/2015.
 */

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import com.nimbusds.jwt.JWTClaimsSet;
import fr.sii.config.application.ApplicationSettings;
import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.email.Email;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowDraft;
import fr.sii.domain.spreadsheet.RowSession;
import fr.sii.service.auth.AuthUtils;
import fr.sii.service.email.EmailingService;
import fr.sii.service.spreadsheet.SpreadsheetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping(value="/api/restricted", produces = "application/json; charset=utf-8")
public class SessionController {

    private SpreadsheetService googleService;

    private EmailingService emailingService;

    private GlobalSettings globalSettings;

    private ApplicationSettings applicationSettings;

    public void setGoogleService(SpreadsheetService googleService) {
        this.googleService = googleService;
    }

    public void setEmailingService(EmailingService emailingService) {
        this.emailingService = emailingService;
    }

    public void setGlobalSettings(GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    /**
     * SESSION
     */
    @RequestMapping(value="/session", method=RequestMethod.POST)
    @ResponseBody public Row postGoogleSpreadsheet(HttpServletRequest req, @Valid @RequestBody RowSession row) throws Exception {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        row.setAdded(new Date());
        row.setUserid(claimsSet.getSubject());

        Row savedRow = googleService.addRow(row);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", row.getFirstname());
        map.put("talk", row.getSessionName());
        map.put("hostname", globalSettings.getHostname());
        map.put("id", savedRow.getAdded().toString());

        Email email = new Email(row.getEmail(),"Confirmation de votre session","confirmed.html",map);
        emailingService.send(email);

        return savedRow;
    }

    @RequestMapping(value="/session", method= RequestMethod.GET)
    @ResponseBody
    public List<Row> getGoogleSpreadsheets(HttpServletRequest req) throws IOException, ServiceException, NotVerifiedException, EntityNotFoundException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        Long userId = Long.parseLong(claimsSet.getSubject());

        return googleService.getRowsSession(userId);
    }

    @RequestMapping(value="/session/{added}", method= RequestMethod.GET)
    @ResponseBody
    public Row getGoogleSpreadsheet(HttpServletRequest req, @PathVariable String added) throws IOException, ServiceException, NotVerifiedException, NotFoundException, ForbiddenException, EntityNotFoundException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        Long userId = Long.parseLong(claimsSet.getSubject());

        return googleService.getRow(added, userId);
    }


    @RequestMapping(value="/session/{added}", method=RequestMethod.PUT)
    @ResponseBody public Row postGoogleSpreadsheetDraftToSession(HttpServletRequest req, @Valid @RequestBody RowSession row, @PathVariable String added) throws Exception {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        row.setAdded(new Date());
        row.setUserid(claimsSet.getSubject());

        Row savedRow = googleService.putRowDraftToSession(row, row.getUserId(), Long.parseLong(added));

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", row.getFirstname());
        map.put("talk", row.getSessionName());
        map.put("hostname", globalSettings.getHostname());
        map.put("id", savedRow.getAdded().toString());

        Email email = new Email(row.getEmail(),"Confirmation de votre session","confirmed.html",map);
        emailingService.send(email);
        return savedRow;
    }

    /**
     * DRAFT
     */
    @RequestMapping(value="/draft", method=RequestMethod.POST)
    @ResponseBody public Row postGoogleSpreadsheetDraft(HttpServletRequest req, @Valid @RequestBody RowDraft row) throws NotVerifiedException, IOException, ServiceException, EntityNotFoundException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        row.setUserid(claimsSet.getSubject());
        return googleService.addRow(row);
    }

    @RequestMapping(value="/draft", method= RequestMethod.GET)
    @ResponseBody
    public List<Row> getGoogleSpreadsheetsDraft(HttpServletRequest req) throws IOException, ServiceException, NotVerifiedException, EntityNotFoundException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        Long userId = Long.parseLong(claimsSet.getSubject());

        return googleService.getRowsDraft(userId);
    }

    @RequestMapping(value="/draft/{added}", method= RequestMethod.GET)
    @ResponseBody
    public Row getGoogleSpreadsheetDraft(HttpServletRequest req, @PathVariable String added) throws IOException, ServiceException, NotVerifiedException, NotFoundException, ForbiddenException, EntityNotFoundException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        Long userId = Long.parseLong(claimsSet.getSubject());

        return googleService.getRow(added, userId);
    }

    @RequestMapping(value="/draft/{added}", method=RequestMethod.DELETE)
    @ResponseBody public void deleteGoogleSpreadsheetDraft(HttpServletRequest req, @PathVariable String added) throws IOException, ServiceException, NotVerifiedException, NotFoundException, ForbiddenException, EntityNotFoundException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        Long userId = Long.parseLong(claimsSet.getSubject());

        googleService.deleteRowDraft(added, userId);
    }

    @RequestMapping(value="/draft/{added}", method=RequestMethod.PUT)
    @ResponseBody public Row putGoogleSpreadsheetDraft(HttpServletRequest req, @Valid @RequestBody RowDraft row, @PathVariable String added) throws NotVerifiedException, ServiceException, ForbiddenException, NotFoundException, IOException, EntityNotFoundException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        row.setAdded(new Date());
        row.setUserid(claimsSet.getSubject());
        return googleService.putRowDraft(row, row.getUserId(), Long.parseLong(added));
    }
}