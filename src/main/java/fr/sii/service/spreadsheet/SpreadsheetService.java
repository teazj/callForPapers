package fr.sii.service.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 * SII
 */
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import fr.sii.config.application.ApplicationSettings;
import fr.sii.config.global.GlobalSettings;
import fr.sii.config.google.GoogleSettings;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import fr.sii.domain.admin.rate.AdminRate;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowResponse;
import fr.sii.repository.spreadsheet.SpreadsheetRepository;
import fr.sii.service.admin.rate.AdminRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpreadsheetService {

    @Autowired
    private SpreadsheetSettings spreadsheetSettings;

    @Autowired
    private GoogleSettings googleSettings;

    @Autowired
    private SpreadsheetRepository spreadsheetRepository;

    @Autowired
    private AdminRateService adminRateService;

    @Autowired
    private GlobalSettings globalSettings;

    @Autowired
    ApplicationSettings applicationSettings;

    private boolean configured;

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public void setSpreadsheetRepository(SpreadsheetRepository spreadsheetRepository) {
        this.spreadsheetRepository = spreadsheetRepository;
    }

    public List<RowResponse> matchRates(List<Row> rs)
    {
        List<RowResponse> nrs = new ArrayList<>();
        for (Row r : rs)
        {
            RowResponse rr = null;

            if(globalSettings.getDatabaseLoaded().equals("true"))
            {
                List<AdminRate> lrs = adminRateService.findByRowId(r.getAdded());
                rr = new RowResponse(r, lrs);
            }
            else
            {
                rr = new RowResponse(r, new ArrayList<AdminRate>());
            }
            nrs.add(rr);
        }
        return nrs;
    }

    public RowResponse matchRates(Row r)
    {
        if(globalSettings.getDatabaseLoaded().equals("true")) {
            List<AdminRate> lrs = adminRateService.findByRowId(r.getAdded());
            return new RowResponse(r, lrs);
        }
        else
        {
            return new RowResponse(r, new ArrayList<AdminRate>());
        }
    }

    @Autowired
    public void login() {
        try {
            spreadsheetRepository.login(spreadsheetSettings, googleSettings);
            applicationSettings.setConfigured(true);
        } catch (ServiceException | IOException | EntityNotFoundException e) {
            applicationSettings.setConfigured(false);
            e.printStackTrace();
        }
    }

    public void login(String refreshToken) {
        try {
            spreadsheetRepository.login(spreadsheetSettings, googleSettings, refreshToken);
            applicationSettings.setConfigured(true);
        } catch (ServiceException | IOException e) {
            applicationSettings.setConfigured(false);
            e.printStackTrace();
        }
    }

    public Row addRow(Row row) throws ServiceException, IOException {
        return spreadsheetRepository.addRow(row);
    }

    public Row putRowDraft(Row row, Long userId, Long added) throws ServiceException, IOException, NotFoundException, ForbiddenException {
        return spreadsheetRepository.putRowDraft(row, userId, added);
    }

    public Row putRowDraftToSession(Row row, Long userId, Long added) throws ServiceException, IOException, NotFoundException, ForbiddenException {
        return spreadsheetRepository.putRowDraftToSession(row, userId, added);
    }

    public List<RowResponse> getRows() throws IOException, ServiceException
    {
        return matchRates(spreadsheetRepository.getRows());
    }

    public RowResponse getRow(String added) throws IOException, ServiceException, NotFoundException {
        return matchRates(spreadsheetRepository.getRow(added));
    }

    public Row getRow(String added, Long userId) throws IOException, ServiceException, NotFoundException, ForbiddenException {
        return spreadsheetRepository.getRow(added, userId);
    }

    public List<Row> deleteRows() throws IOException, ServiceException
    {
        return spreadsheetRepository.deleteRows();
    }

    public void deleteRowDraft(String added, Long userId) throws IOException, ServiceException, NotFoundException, ForbiddenException {
        spreadsheetRepository.deleteRowDraft(added, userId);
    }

    public List<RowResponse> getRowsSession() throws IOException, ServiceException
    {
        return matchRates(spreadsheetRepository.getRowsSession());
    }

    public List<Row> getRowsSession(Long userId) throws IOException, ServiceException
    {
        return spreadsheetRepository.getRowsSession(userId);
    }

    public List<Row> getRowsDraft() throws IOException, ServiceException
    {
        return spreadsheetRepository.getRowsDraft();
    }

    public List<Row> getRowsDraft(Long userId) throws IOException, ServiceException
    {
        return spreadsheetRepository.getRowsDraft(userId);
    }
}