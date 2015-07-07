package fr.sii.service.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 * SII
 */

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.admin.rate.AdminRate;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowResponse;
import fr.sii.repository.spreadsheet.SpreadsheetRepository;
import fr.sii.service.admin.comment.AdminCommentService;
import fr.sii.service.admin.rate.AdminRateService;
import fr.sii.service.admin.user.AdminUserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpreadsheetService {

    private SpreadsheetRepository spreadsheetRepository;

    private AdminRateService adminRateService;

    private AdminCommentService adminCommentService;

    private GlobalSettings globalSettings;

    private AdminUserService adminUserServiceCustom;

    public void setAdminUserService(AdminUserService adminUserServiceCustom) {
        this.adminUserServiceCustom = adminUserServiceCustom;
    }

    public void setAdminRateService(AdminRateService adminRateService) {
        this.adminRateService = adminRateService;
    }

    public void setGlobalSettings(GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    public void setAdminCommentService(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

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

    public List<RowResponse> matchRatesAndComment(List<Row> rs)
    {
        List<RowResponse> nrs = new ArrayList<>();
        for (Row r : rs)
        {
            RowResponse rr = null;

            if(globalSettings.getDatabaseLoaded().equals("true"))
            {
                List<AdminRate> lrs = adminRateService.findByRowId(r.getAdded());
                List<AdminComment> lcs = adminCommentService.findByRowId(r.getAdded());
                rr = new RowResponse(r, lrs, lcs, adminUserServiceCustom.getCurrentUser().getEntityId());
            }
            else
            {
                rr = new RowResponse(r, new ArrayList<AdminRate>(), new ArrayList<AdminComment>());
            }
            nrs.add(rr);
        }
        return nrs;
    }

    public RowResponse matchRatesAndComment(Row r)
    {
        if(globalSettings.getDatabaseLoaded().equals("true")) {
            List<AdminRate> lrs = adminRateService.findByRowId(r.getAdded());
            List<AdminComment> lcs = adminCommentService.findByRowId(r.getAdded());
            return new RowResponse(r, lrs, lcs, adminUserServiceCustom.getCurrentUser().getEntityId());
        }
        else
        {
            return new RowResponse(r, new ArrayList<AdminRate>(), new ArrayList<AdminComment>());
        }
    }

    public void login() {
        try {
            spreadsheetRepository.login();
        } catch (EntityNotFoundException | ServiceException | IOException e) {
            //e.printStackTrace();
        }
    }

    public void login(String refreshToken) {
        try {
            spreadsheetRepository.login(refreshToken);
        } catch (IOException | ServiceException e) {
            //e.printStackTrace();
        }
    }

    public Row addRow(Row row) throws ServiceException, IOException, EntityNotFoundException {
        return spreadsheetRepository.addRow(row);
    }

    public Row putRowDraft(Row row, Long userId, Long added) throws ServiceException, IOException, NotFoundException, ForbiddenException, EntityNotFoundException {
        return spreadsheetRepository.putRowDraft(row, userId, added);
    }

    public Row putRowDraftToSession(Row row, Long userId, Long added) throws ServiceException, IOException, NotFoundException, ForbiddenException, EntityNotFoundException {
        return spreadsheetRepository.putRowDraftToSession(row, userId, added);
    }

    public List<RowResponse> getRows() throws IOException, ServiceException, EntityNotFoundException {
        return matchRatesAndComment(spreadsheetRepository.getRows());
    }

    public RowResponse getRow(String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException {
        return matchRatesAndComment(spreadsheetRepository.getRow(added));
    }

    public Row getRow(String added, Long userId) throws IOException, ServiceException, NotFoundException, ForbiddenException, EntityNotFoundException {
        return spreadsheetRepository.getRow(added, userId);
    }

    public List<Row> deleteRows() throws IOException, ServiceException, EntityNotFoundException {
        return spreadsheetRepository.deleteRows();
    }

    public void deleteRowDraft(String added, Long userId) throws IOException, ServiceException, NotFoundException, ForbiddenException, EntityNotFoundException {
        spreadsheetRepository.deleteRowDraft(added, userId);
    }

    public void deleteRow(String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException {
        spreadsheetRepository.deleteRow(added);
    }

    public List<RowResponse> getRowsSession() throws IOException, ServiceException, EntityNotFoundException {
        return matchRatesAndComment(spreadsheetRepository.getRowsSession());
    }

    public List<Row> getRowsSession(Long userId) throws IOException, ServiceException, EntityNotFoundException {
        return spreadsheetRepository.getRowsSession(userId);
    }

    public List<Row> getRowsDraft() throws IOException, ServiceException, EntityNotFoundException {
        return spreadsheetRepository.getRowsDraft();
    }

    public List<Row> getRowsDraft(Long userId) throws IOException, ServiceException, EntityNotFoundException {
        return spreadsheetRepository.getRowsDraft(userId);
    }
}