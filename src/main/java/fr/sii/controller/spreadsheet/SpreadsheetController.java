package fr.sii.controller.spreadsheet;

/**
 * Created by tmaugin on 01/04/2015.
 */

import com.google.gdata.util.ServiceException;
import fr.sii.service.spreadsheet.SpreadsheetService;
import fr.sii.domain.spreadsheet.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping(produces = "application/json; charset=utf-8")
public class SpreadsheetController {

    @Autowired
    private SpreadsheetService googleService;


    @RequestMapping(value="/{event}/session", method=RequestMethod.POST)
    @ResponseBody public Row postGoogleSpreadsheet(@PathVariable String event, @Valid @RequestBody Row row) throws IOException, ServiceException {
        return googleService.addRow(row);
    }

    @RequestMapping(value="/{event}/session", method= RequestMethod.GET)
    @ResponseBody
    public List<Row> getGoogleSpreadsheets(@PathVariable String event) throws IOException, ServiceException {
        return googleService.getRows();
    }

    @RequestMapping(value="/{event}/session/{added}", method= RequestMethod.GET)
    @ResponseBody
    public Row getGoogleSpreadsheet(@PathVariable String event, @PathVariable String added) throws IOException, ServiceException {
        return googleService.getRow(added);
    }

    @RequestMapping(value="/{event}/session", method=RequestMethod.DELETE)
    @ResponseBody public List<Row> deleteGoogleSpreadsheet(@PathVariable String event) throws IOException, ServiceException {
        return googleService.deleteRows();
    }
}