package fr.sii.controller.admin.session;

/**
 * Created by tmaugin on 15/05/2015.
 */

import com.google.gdata.util.ServiceException;
import fr.sii.config.application.ApplicationSettings;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.RowResponse;
import fr.sii.service.email.EmailingService;
import fr.sii.service.spreadsheet.SpreadsheetService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SpreadsheetService googleService;

    @Autowired
    private EmailingService emailingService;

    @Autowired
    private ApplicationSettings applicationSettings;

    @RequestMapping(value="/session", method= RequestMethod.GET)
    @ResponseBody
    public List<RowResponse> getGoogleSpreadsheets() throws IOException, ServiceException {
        return googleService.getRowsSession();
    }

    @RequestMapping(value="/session/{added}", method= RequestMethod.GET)
    @ResponseBody
    public RowResponse getGoogleSpreadsheet(@PathVariable String added) throws IOException, ServiceException, NotFoundException {
        return googleService.getRow(added);
    }
}