package fr.sii.service.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 */
import com.google.gdata.util.ServiceException;
import fr.sii.config.global.GlobalSettings;
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
    private SpreadsheetSettings googleSettings;

    @Autowired
    private SpreadsheetRepository googleRepository;

    @Autowired
    private AdminRateService adminRateService;

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
    public void login() throws ServiceException, IOException {
        googleRepository.login(googleSettings);
    }

    public Row addRow(Row row) throws ServiceException, IOException {
        return googleRepository.addRow(row);
    }

    public Row putRowDraft(Row row, Long userId, Long added) throws ServiceException, IOException, NotFoundException, ForbiddenException {
        return googleRepository.putRowDraft(row, userId, added);
    }

    public Row putRowDraftToSession(Row row, Long userId, Long added) throws ServiceException, IOException, NotFoundException, ForbiddenException {
        return googleRepository.putRowDraftToSession(row, userId, added);
    }

    public List<RowResponse> getRows() throws IOException, ServiceException
    {
        return matchRates(googleRepository.getRows());
    }

    public RowResponse getRow(String added) throws IOException, ServiceException, NotFoundException {
        return matchRates(googleRepository.getRow(added));
    }

    public Row getRow(String added, Long userId) throws IOException, ServiceException, NotFoundException, ForbiddenException {
        return googleRepository.getRow(added, userId);
    }

    public List<Row> deleteRows() throws IOException, ServiceException
    {
        return googleRepository.deleteRows();
    }

    public void deleteRowDraft(String added, Long userId) throws IOException, ServiceException, NotFoundException, ForbiddenException {
        googleRepository.deleteRowDraft(added, userId);
    }

    public List<RowResponse> getRowsSession() throws IOException, ServiceException
    {
        return matchRates(googleRepository.getRowsSession());
    }

    public List<Row> getRowsSession(Long userId) throws IOException, ServiceException
    {
        return googleRepository.getRowsSession(userId);
    }

    public List<Row> getRowsDraft() throws IOException, ServiceException
    {
        return googleRepository.getRowsDraft();
    }

    public List<Row> getRowsDraft(Long userId) throws IOException, ServiceException
    {
        return googleRepository.getRowsDraft(userId);
    }
}