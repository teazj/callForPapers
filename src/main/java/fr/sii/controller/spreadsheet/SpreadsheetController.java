package fr.sii.controller.spreadsheet;

/**
 * Created by tmaugin on 01/04/2015.
 */

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gdata.util.ServiceException;
import fr.sii.config.application.ApplicationSettings;
import fr.sii.domain.email.Email;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowResponse;
import fr.sii.repository.email.Templating;
import fr.sii.service.email.EmailingService;
import fr.sii.service.spreadsheet.SpreadsheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping(produces = "application/json; charset=utf-8")
public class SpreadsheetController {

    @Autowired
    private SpreadsheetService googleService;

    @Autowired
    private EmailingService emailingService;

    @Autowired
    private ApplicationSettings applicationSettings;


    @RequestMapping(value="/postSession", method=RequestMethod.POST)
    @ResponseBody public Row postGoogleSpreadsheet(@Valid @RequestBody Row row) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", row.getFirstname());
        map.put("talk", row.getSessionName());

        Email email = new Email(row.getEmail(),"Confirmation de votre talk","confirmed.html",map);
        emailingService.send(email);
        return googleService.addRow(row);
    }

    @RequestMapping(value="/session", method= RequestMethod.GET)
    @ResponseBody
    public List<RowResponse> getGoogleSpreadsheets() throws IOException, ServiceException {
        return googleService.getRows();
    }

    @RequestMapping(value="/session/{added}", method= RequestMethod.GET)
    @ResponseBody
    public RowResponse getGoogleSpreadsheet(@PathVariable String added) throws IOException, ServiceException {
        return googleService.getRow(added);
    }

    @RequestMapping(value="/session", method=RequestMethod.DELETE)
    @ResponseBody public List<Row> deleteGoogleSpreadsheet() throws IOException, ServiceException {
        return googleService.deleteRows();
    }
}