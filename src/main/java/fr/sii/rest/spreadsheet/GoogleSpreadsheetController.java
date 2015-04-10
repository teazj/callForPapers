package fr.sii.rest.spreadsheet;

/**
 * Created by tmaugin on 01/04/2015.
 */
import java.io.IOException;
import java.util.List;

import com.google.gdata.util.ServiceException;
import fr.sii.persistance.spreadsheet.SpreadsheetService;
import fr.sii.persistance.spreadsheet.RowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class GoogleSpreadsheetController {

    @Autowired
    private SpreadsheetService googleService;

    @RequestMapping(value="/{event}/session", method=RequestMethod.POST)
    @ResponseBody public RowModel postGoogleSpreadsheet(@PathVariable String event, @Valid @RequestBody RowModel row) throws IOException, ServiceException {
        return googleService.addRow(row);
    }

    @RequestMapping(value="/{event}/session", method=RequestMethod.GET)
    @ResponseBody public List<RowModel> getGoogleSpreadsheet(@PathVariable String event) throws IOException, ServiceException {
        return googleService.getRows();
    }

    @RequestMapping(value="/{event}/session", method=RequestMethod.DELETE)
    @ResponseBody public List<RowModel> deleteGoogleSpreadsheet(@PathVariable String event) throws IOException, ServiceException {
        return googleService.deleteRows();
    }
}