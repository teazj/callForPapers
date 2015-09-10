package fr.sii.service.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 * SII
 */

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.gdata.util.ServiceException;
import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.admin.rate.AdminRate;
import fr.sii.domain.admin.session.AdminViewedSession;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowResponse;
import fr.sii.domain.user.UserProfil;
import fr.sii.repository.spreadsheet.SpreadsheetRepository;
import fr.sii.service.admin.comment.AdminCommentService;
import fr.sii.service.admin.rate.AdminRateService;
import fr.sii.service.admin.session.AdminViewedSessionService;
import fr.sii.service.admin.user.AdminUserService;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SpreadsheetService {

    private SpreadsheetRepository spreadsheetRepository;

    private AdminRateService adminRateService;

    private AdminCommentService adminCommentService;

    private GlobalSettings globalSettings;

    private AdminUserService adminUserServiceCustom;

    private AdminViewedSessionService adminViewedSessionService;

    private final String DRAFTS_KEY = "Drafts";

    private final String SESSIONS_KEY = "Sessions";
    private final String SESSIONS_KEY_ADMIN = "SessionsAdmin";

    private Cache cache;

    @PostConstruct
    public void prepareCache() {
        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        Map properties = new HashMap<>();
        properties.put(GCacheFactory.EXPIRATION_DELTA, TimeUnit.MINUTES.toSeconds(5));
        cache = cacheFactory.createCache(properties);
    }

    public void setAdminViewedSessionService(AdminViewedSessionService adminViewedSessionService) {
        this.adminViewedSessionService = adminViewedSessionService;
    }

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

    public List<RowResponse> matchRatesAndComment(List<Row> rows) throws NotFoundException {
        List<RowResponse> rowResponses = new ArrayList<>();
        for (Row row : rows)
        {
            RowResponse rowResponse = null;

            if(globalSettings.getDatabaseLoaded().equals("true"))
            {
                List<AdminRate> adminRates = adminRateService.findByRowId(row.getAdded());
                List<AdminComment> adminComments = adminCommentService.findByRowId(row.getAdded());
                AdminUser adminUser = adminUserServiceCustom.getCurrentUser();
                AdminRate adminRate = adminRateService.findByRowIdAndUserId(row.getAdded(), adminUser.getEntityId());
                try {
                    AdminViewedSession viewedSession = adminViewedSessionService.findByRowIdAndUserId(row.getAdded(), adminUser.getEntityId());
                    rowResponse = new RowResponse(row, adminRates, adminComments, adminUser.getEntityId(), viewedSession.getLastSeen());
                } catch (NotFoundException e) {
                    // if lastSeen not found => null
                    rowResponse = new RowResponse(row, adminRates, adminComments, adminUser.getEntityId(), null);
                }
                rowResponse.setReviewed(adminRate != null);
            }
            else
            {
                // for testing purpose
                rowResponse = new RowResponse(row, new ArrayList<AdminRate>(), new ArrayList<AdminComment>());
            }
            rowResponses.add(rowResponse);
        }
        return rowResponses;
    }

    public RowResponse matchRatesAndComment(Row row) throws NotFoundException {
        if(globalSettings.getDatabaseLoaded().equals("true")) {
            List<AdminRate> adminRates = adminRateService.findByRowId(row.getAdded());
            List<AdminComment> adminComments = adminCommentService.findByRowId(row.getAdded());
            AdminUser adminUser = adminUserServiceCustom.getCurrentUser();
            try {
                AdminViewedSession viewedSession = adminViewedSessionService.findByRowIdAndUserId(row.getAdded(), adminUser.getEntityId());
                return new RowResponse(row, adminRates, adminComments, adminUser.getEntityId(), viewedSession.getLastSeen());
            } catch (NotFoundException e) {
                return new RowResponse(row, adminRates, adminComments, adminUser.getEntityId(), null);
            }
        }
        else
        {
            return new RowResponse(row, new ArrayList<AdminRate>(), new ArrayList<AdminComment>());
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

    public void updateProfilSessions(Row row, Long userId) throws EntityNotFoundException, IOException, ServiceException {
        spreadsheetRepository.updateProfilSessions(row, userId);
    }

    public void updateProfilSessions(UserProfil profil, Long userId) throws EntityNotFoundException, IOException, ServiceException {
        spreadsheetRepository.updateProfilSessions(profil, userId);
    }

    public List<RowResponse> getRows() throws IOException, ServiceException, EntityNotFoundException, NotFoundException {
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

    public List<RowResponse> getRowsSessionAdmin() throws IOException, ServiceException, EntityNotFoundException, NotFoundException {

        List<RowResponse> rowResponses = null;
        try {
            // Use cache
            rowResponses = (List<RowResponse>) cache.get(SESSIONS_KEY_ADMIN);
            if(rowResponses == null) {
                rowResponses = matchRatesAndComment(spreadsheetRepository.getRowsSession());
                cache.put(SESSIONS_KEY_ADMIN, rowResponses);
            }
        } catch (CacheException e) {
            e.printStackTrace();
            return matchRatesAndComment(spreadsheetRepository.getRowsSession());
        }

        // Add information for customized data (replace wrong cached ones)
        AdminUser adminUser = adminUserServiceCustom.getCurrentUser();
        List<AdminRate> adminRates = adminRateService.findByUserIdNoMatch(adminUser.getEntityId());
        List<AdminViewedSession> adminViewedSessions = adminViewedSessionService.findByUserId(adminUser.getEntityId());
        int matchRates = 0;
        int matchViewedSessions = 0;
        for(RowResponse rowResponse : rowResponses) {

            rowResponse.setReviewed(false);
            //all rates are setted
            if(matchRates != adminRates.size()) {
                // Reviewed or not
                boolean reviewed = false;
                int i = 0;
                while (i < adminRates.size() && !reviewed) {
                    if (adminRates.get(i).getRowId().longValue() == rowResponse.getAdded().longValue()) {
                        reviewed = true;
                        matchRates++;
                    }
                    i++;
                }
                rowResponse.setReviewed(reviewed);
            }

            rowResponse.setLastSeen(null);
            //all viewedSessions are setted
            if(matchViewedSessions != adminViewedSessions.size()) {
                // LastSeen
                Date lastSeen = null;
                int i = 0;
                while(i < adminViewedSessions.size() && lastSeen == null) {
                    if(adminViewedSessions.get(i).getRowId().longValue() == rowResponse.getAdded().longValue()) {
                        lastSeen = adminViewedSessions.get(i).getLastSeen();
                        matchViewedSessions++;
                    }
                    i++;
                }
                rowResponse.setLastSeen(lastSeen);
            }
        }
        return rowResponses;
    }
    public List<Row> getRowsSession() throws IOException, ServiceException, EntityNotFoundException {
        
        List<Row> rows = null;
        try {
            // Use cache
            rows = (List<Row>) cache.get(SESSIONS_KEY);
            if(rows == null) {
                rows = spreadsheetRepository.getRowsSession();
                cache.put(SESSIONS_KEY, rows);
            }
        } catch (CacheException e) {
            e.printStackTrace();
            return spreadsheetRepository.getRowsSession();
        }
        return rows;
    }

    public List<Row> getRowsSession(Long userId) throws IOException, ServiceException, EntityNotFoundException {
        return spreadsheetRepository.getRowsSession(userId);
    }

    public Row changeRowTrack(Long added, String track) throws ServiceException, IOException, NotFoundException, EntityNotFoundException {
        return spreadsheetRepository.changeRowTrack(added, track);
    }

    public List<Row> getRowsDraft() throws IOException, ServiceException, EntityNotFoundException {
        List<Row> rowsDraft = null;
        try {
            rowsDraft = (List<Row>) cache.get(DRAFTS_KEY);
            if(rowsDraft == null) {
                rowsDraft = spreadsheetRepository.getRowsDraft();
                cache.put(DRAFTS_KEY, rowsDraft);
            }
        } catch (CacheException e) {
            e.printStackTrace();
            return spreadsheetRepository.getRowsDraft();
        }
        return rowsDraft;
    }

    public List<Row> getRowsDraft(Long userId) throws IOException, ServiceException, EntityNotFoundException {
        return spreadsheetRepository.getRowsDraft(userId);
    }
}