package fr.sii.controller.admin.session;

/**
 * Created by tmaugin on 15/05/2015.
 */

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import fr.sii.config.application.ApplicationSettings;
import fr.sii.domain.admin.comment.AdminComment;
import fr.sii.domain.admin.session.AdminViewedSession;
import fr.sii.domain.admin.session.Track;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.domain.spreadsheet.RowResponse;
import fr.sii.service.admin.comment.AdminCommentService;
import fr.sii.service.admin.session.AdminViewedSessionService;
import fr.sii.service.admin.user.AdminUserService;
import fr.sii.service.email.EmailingService;
import fr.sii.service.spreadsheet.SpreadsheetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value="api/admin", produces = "application/json; charset=utf-8")
public class AdminSessionController {

    private SpreadsheetService googleService;

    private EmailingService emailingService;

    private ApplicationSettings applicationSettings;


    private AdminViewedSessionService adminViewedSessionService;

    private AdminCommentService adminCommentService;

    private AdminUserService adminUserServiceCustom;

    public void setAdminCommentService(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    public void setAdminViewedSessionService(AdminViewedSessionService adminViewedSessionService) {
        this.adminViewedSessionService = adminViewedSessionService;
    }

    public void setAdminUserServiceCustom(AdminUserService adminUserServiceCustom) {
        this.adminUserServiceCustom = adminUserServiceCustom;
    }

    public void setGoogleService(SpreadsheetService googleService) {
        this.googleService = googleService;
    }

    public void setEmailingService(EmailingService emailingService) {
        this.emailingService = emailingService;
    }

    public void setApplicationSettings(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    /**
     * Get all sessions
     * @return
     * @throws IOException
     * @throws ServiceException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/sessions", method= RequestMethod.GET)
    @ResponseBody
    public List<RowResponse> getGoogleSpreadsheets() throws IOException, ServiceException, EntityNotFoundException, NotFoundException {
        return googleService.getRowsSessionAdmin();
    }

    /**
     * Get all drafts
     * @return
     * @throws IOException
     * @throws ServiceException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/drafts", method= RequestMethod.GET)
    @ResponseBody
    public List<Row> getGoogleSpreadsheetsDraft() throws IOException, ServiceException, EntityNotFoundException {
        return googleService.getRowsDraft();
    }

    /**
     * Get a specific session
     * @param added
     * @return
     * @throws IOException
     * @throws ServiceException
     * @throws NotFoundException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/sessions/{added}", method= RequestMethod.GET)
    @ResponseBody
    public RowResponse getGoogleSpreadsheet(@PathVariable String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException {
        return googleService.getRow(added);
    }

    /**
     * Delete a session
     * @param added
     * @throws IOException
     * @throws ServiceException
     * @throws NotFoundException
     * @throws EntityNotFoundException
     */
    @RequestMapping(value="/sessions/{added}", method= RequestMethod.DELETE)
    @ResponseBody
    public void deleteGoogleSpreadsheet(@PathVariable String added) throws IOException, ServiceException, NotFoundException, EntityNotFoundException {
        googleService.deleteRow(added);
    }

    /**
     * Set session as viewed
     * @param added
     * @return
     * @throws NotFoundException
     */
    @RequestMapping(value="/sessions/viewed/{added}", method= RequestMethod.POST)
    @ResponseBody
    public AdminViewedSession postGoogleSpreadsheetViewed(@PathVariable String added) throws NotFoundException {
        AdminUser currentUser = adminUserServiceCustom.getCurrentUser();
        if(currentUser == null)
        {
            throw new NotFoundException("User not found");
        }
        return adminViewedSessionService.put(Long.parseLong(added),currentUser.getEntityId(), new AdminViewedSession(Long.parseLong(added), currentUser.getEntityId(), new Date()));
    }

    /**
     * Edit session's track
     * @param added
     * @param track
     * @return
     * @throws NotFoundException
     * @throws ServiceException
     * @throws EntityNotFoundException
     * @throws IOException
     */
    @RequestMapping(value="/sessions/track/{added}", method= RequestMethod.PUT)
    @ResponseBody
    public Row putGoogleSpreadsheetTrack(@PathVariable String added, @Valid @RequestBody Track track) throws NotFoundException, ServiceException, EntityNotFoundException, IOException {
        AdminUser adminUser = adminUserServiceCustom.getCurrentUser();
        AdminComment adminComment = new AdminComment();
        Row row = googleService.getRow(added);
        adminComment.setComment("Catégorie changée de \"" + row.getTrack() + "\" à \"" + track.getTrack() + "\".");
        adminComment.setUserId(adminUser.getEntityId());
        adminComment.setRowId(Long.parseLong(added));
        adminCommentService.save(adminComment);
        return googleService.changeRowTrack(Long.parseLong(added), track.getTrack());
    }
}