package fr.sii.service.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 */
import com.google.gdata.util.ServiceException;
import fr.sii.config.global.GlobalSettings;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import fr.sii.domain.rate.Rate;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowResponse;
import fr.sii.repository.spreadsheet.SpreadsheetRepository;
import fr.sii.service.rate.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpreadsheetService {

    @Autowired
    private SpreadsheetSettings googleSettings;

    @Autowired
    private SpreadsheetRepository googleRepository;

    @Autowired
    private RateService rateService;

    @Autowired
    private GlobalSettings globalSettings;

    public List<RowResponse> matchRates(List<Row> rs)
    {
        List<RowResponse> nrs = new ArrayList<>();
        for (Row r : rs)
        {
            RowResponse rr = null;

            if(globalSettings.getDatabaseLoaded().equals("true"))
            {
                List<Rate> lrs = rateService.findByRowId(r.getAdded());
                rr = new RowResponse(r, lrs);
            }
            else
            {
                rr = new RowResponse(r, new ArrayList<Rate>());
            }
            nrs.add(rr);
        }
        return nrs;
    }

    public RowResponse matchRates(Row r)
    {
        if(globalSettings.getDatabaseLoaded().equals("true")) {
            List<Rate> lrs = rateService.findByRowId(r.getAdded());
            return new RowResponse(r, lrs);
        }
        else
        {
            return new RowResponse(r, new ArrayList<Rate>());
        }
    }

    @Autowired
    public void login() throws ServiceException, IOException {
        googleRepository.login(googleSettings);
    }

    public Row addRow(Row row) throws ServiceException, IOException {
        return googleRepository.addRow(row);
    }

    public List<RowResponse> getRows() throws IOException, ServiceException
    {
        return matchRates(googleRepository.getRows());
    }

    public RowResponse getRow(String added) throws IOException, ServiceException
    {
        return matchRates(googleRepository.getRow(added));
    }

    public List<Row> deleteRows() throws IOException, ServiceException
    {
        return googleRepository.deleteRows();
    }
}