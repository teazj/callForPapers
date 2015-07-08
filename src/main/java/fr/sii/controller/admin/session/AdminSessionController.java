package fr.sii.controller.admin.session;

/**
 * Created by tmaugin on 15/05/2015.
 */

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import fr.sii.config.application.ApplicationSettings;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowResponse;
import fr.sii.service.email.EmailingService;
import fr.sii.service.spreadsheet.SpreadsheetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping(value="api/admin", produces = "application/json; charset=utf-8")
public class AdminSessionController {

    private SpreadsheetService googleService;

    private EmailingService emailingService;

    private ApplicationSettings applicationSettings;

    public void setGoogleService(SpreadsheetService googleService) {
        this.googleService = googleService;
    }

    public void setEmailingService(EmailingService emailingService) {
        this.emailingService = emailingService;
    }

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    @RequestMapping(value="/sessions", method= RequestMethod.GET)
    @ResponseBody
    public List<RowResponse> getGoogleSpreadsheets() throws IOException, ServiceException, EntityNotFoundException {
        return googleService.getRowsSession();
    }

    @RequestMapping(value="/drafts", method= RequestMethod.GET)
    @ResponseBody
    public List<Row> getGoogleSpreadsheetsDraft() throws IOException, ServiceException, EntityNotFoundException {
        return googleService.getRowsDraft();
    }

    @RequestMapping(value="/sessions/{added}", method= RequestMethod.GET)
    @ResponseBody
    public RowResponse getGoogleSpreadsheet(@PathVariable String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException {
        return googleService.getRow(added);
    }

    @RequestMapping(value="/sessions/{added}", method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteGoogleSpreadsheet(@PathVariable String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException {
        googleService.deleteRow(added);
    }
}