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
     * Add a session to the spreadsheet
     * @param req
     * @param row
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/sessions", method=RequestMethod.POST)
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

    /**
     * Get all session for the current user
     * @param req
     * @return
     * @throws IOException
     * @throws ServiceException
     * @throws NotVerifiedException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/sessions", method= RequestMethod.GET)
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

    /**
     * Get a session
     * @param req
     * @param added
     * @return
     * @throws IOException
     * @throws ServiceException
     * @throws NotVerifiedException
     * @throws NotFoundException
     * @throws ForbiddenException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/sessions/{added}", method= RequestMethod.GET)
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

    /**
     * Change a draft to a session
     * @param req
     * @param row
     * @param added
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/sessions/{added}", method=RequestMethod.PUT)
    @ResponseBody public Row postGoogleSpreadsheetDraftToSession(HttpServletRequest req, @Valid @RequestBody RowSession row, @PathVariable String added) throws Exception {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        row.setAdded(new Date());
        row.setUserid(claimsSet.getSubject());

        googleService.updateProfilSessions(row, row.getUserId());
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
     * Add a new draft
     * @param req
     * @param row
     * @return
     * @throws NotVerifiedException
     * @throws IOException
     * @throws ServiceException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/drafts", method=RequestMethod.POST)
    @ResponseBody public Row postGoogleSpreadsheetDraft(HttpServletRequest req, @Valid @RequestBody RowDraft row) throws NotVerifiedException, IOException, ServiceException, EntityNotFoundException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        row.setUserid(claimsSet.getSubject());
        return googleService.addRow(row);
    }

    /**
     * Get all drafts for current user
     * @param req
     * @return
     * @throws IOException
     * @throws ServiceException
     * @throws NotVerifiedException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/drafts", method= RequestMethod.GET)
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

    /**
     * Get a draft
     * @param req
     * @param added
     * @return
     * @throws IOException
     * @throws ServiceException
     * @throws NotVerifiedException
     * @throws NotFoundException
     * @throws ForbiddenException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/drafts/{added}", method= RequestMethod.GET)
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

    /**
     * Delete a draft
     * @param req
     * @param added
     * @throws IOException
     * @throws ServiceException
     * @throws NotVerifiedException
     * @throws NotFoundException
     * @throws ForbiddenException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/drafts/{added}", method=RequestMethod.DELETE)
    @ResponseBody public void deleteGoogleSpreadsheetDraft(HttpServletRequest req, @PathVariable String added) throws IOException, ServiceException, NotVerifiedException, NotFoundException, ForbiddenException, EntityNotFoundException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }
        Long userId = Long.parseLong(claimsSet.getSubject());

        googleService.deleteRowDraft(added, userId);
    }

    /**
     * Edit a draft
     * @param req
     * @param row
     * @param added
     * @return
     * @throws NotVerifiedException
     * @throws ServiceException
     * @throws ForbiddenException
     * @throws NotFoundException
     * @throws IOException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/drafts/{added}", method=RequestMethod.PUT)
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