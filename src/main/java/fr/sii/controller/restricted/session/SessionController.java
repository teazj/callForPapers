package fr.sii.controller.restricted.session;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.sii.controller.restricted.RestrictedController;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.dto.TalkUser;
import fr.sii.dto.TrackDto;
import fr.sii.entity.Talk;
import fr.sii.entity.TalkFormat;
import fr.sii.entity.User;
import fr.sii.repository.UserRepo;
import fr.sii.service.TalkUserService;
import fr.sii.service.email.EmailingService;

@RestController
@RequestMapping(value = "/api/restricted", produces = "application/json; charset=utf-8")
public class SessionController extends RestrictedController {

    @Autowired
    private EmailingService emailingService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TalkUserService talkService;

    /**
     * Add a session
     */
    @RequestMapping(value = "/sessions", method = RequestMethod.POST)
    public TalkUser submitTalk(HttpServletRequest httpServletRequest, @Valid @RequestBody TalkUser talkUser) throws Exception {
        User user = userRepo.findOne(retrieveUserId(httpServletRequest));
        TalkUser savedTalk = talkService.submitTalk(user, talkUser);

        if (user != null && savedTalk != null) {
            Locale userPreferredLocale = httpServletRequest.getLocale();
            emailingService.sendConfirmed(user, savedTalk, userPreferredLocale);
        }

        return savedTalk;
    }

    /**
     * Get all session for the current user
     */
    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public List<TalkUser> listTalks(HttpServletRequest req) throws NotVerifiedException {
        int userId = retrieveUserId(req);

        return talkService.findAll(userId, Talk.State.CONFIRMED, Talk.State.ACCEPTED, Talk.State.REFUSED);
    }

    /**
     * Get a session
     */
    @RequestMapping(value = "/sessions/{talkId}", method = RequestMethod.GET)
    public TalkUser getGoogleSpreadsheet(HttpServletRequest req, @PathVariable Integer talkId) throws NotVerifiedException {
        int userId = retrieveUserId(req);

        return talkService.getOne(userId, talkId);
    }

    /**
     * Change a draft to a session
     */
    @RequestMapping(value = "/sessions/{talkId}", method = RequestMethod.PUT)
    public TalkUser submitDraftToTalk(HttpServletRequest httpServletRequest, @Valid @RequestBody TalkUser talkUser, @PathVariable Integer talkId) throws Exception {
        int userId = retrieveUserId(httpServletRequest);
        User user = userRepo.findOne(userId);

        talkUser.setId(talkId);
        TalkUser savedTalk = talkService.submitDraftToTalk(userId, talkUser);

        if (user != null && savedTalk != null) {
            Locale userPreferredLocale = httpServletRequest.getLocale();
            emailingService.sendConfirmed(user, savedTalk, userPreferredLocale);
        }

        return savedTalk;
    }

    /**
     * Add a new draft
     */
    @RequestMapping(value = "/drafts", method = RequestMethod.POST)
    public TalkUser addDraft(HttpServletRequest req, @Valid @RequestBody TalkUser talkUser) throws NotVerifiedException {
        int userId = retrieveUserId(req);
        return talkService.addDraft(userId, talkUser);
    }

    /**
     * Get all drafts for current user
     */
    @RequestMapping(value = "/drafts", method = RequestMethod.GET)
    public List<TalkUser> listDrafts(HttpServletRequest req) throws NotVerifiedException {
        int userId = retrieveUserId(req);

        return talkService.findAll(userId, Talk.State.DRAFT);
    }

    /**
     * Get a draft
     */
    @RequestMapping(value = "/drafts/{talkId}", method = RequestMethod.GET)
    public TalkUser getDraft(HttpServletRequest req, @PathVariable Integer talkId) throws NotVerifiedException {
        int userId = retrieveUserId(req);

        return talkService.getOne(userId, talkId);
    }

    /**
     * Delete a draft
     */
    @RequestMapping(value = "/drafts/{talkId}", method = RequestMethod.DELETE)
    public TalkUser deleteDraft(HttpServletRequest req, @PathVariable Integer talkId) throws NotVerifiedException {
        int userId = retrieveUserId(req);

        return talkService.deleteDraft(userId, talkId);
    }

    /**
     * Edit a draft
     */
    @RequestMapping(value = "/drafts/{talkId}", method = RequestMethod.PUT)
    public TalkUser editDraft(HttpServletRequest req, @Valid @RequestBody TalkUser talkUser, @PathVariable Integer talkId) throws NotVerifiedException {
        int userId = retrieveUserId(req);

        talkUser.setId(talkId);
        return talkService.editDraft(userId, talkUser);
    }

    /**
     * Obtain list of talk formats
     */
    @RequestMapping(value = "talk/formats")
    public List<TalkFormat> getTalkFormat() {
        return talkService.getTalkFormat();
    }

    /**
     * Get all session for the current user
     */
    @RequestMapping(value = "talk/tracks", method = RequestMethod.GET)
    public List<TrackDto> getTrack() throws NotVerifiedException {
        return talkService.getTracks();
    }

}
