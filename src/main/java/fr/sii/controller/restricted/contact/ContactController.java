package fr.sii.controller.restricted.contact;

import fr.sii.controller.restricted.RestrictedController;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.dto.CommentUser;
import fr.sii.dto.TalkUser;
import fr.sii.entity.User;
import fr.sii.service.CommentUserService;
import fr.sii.service.TalkUserService;
import fr.sii.service.email.EmailingService;
import fr.sii.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "api/restricted/sessions/{talkId}/contacts", produces = "application/json; charset=utf-8")
public class ContactController extends RestrictedController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailingService emailingService;

    @Autowired
    private CommentUserService commentService;

    @Autowired
    private TalkUserService talkUserService;

    /**
     * Get all contact message for a given session
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<CommentUser> getAll(@PathVariable int talkId, HttpServletRequest req) throws NotVerifiedException {
        int userId = retrieveUserId(req);

        return commentService.findAll(userId, talkId);
    }

    /**
     * Add a contact message for the given session
     */
    @RequestMapping(method = RequestMethod.POST)
    public CommentUser postContact(@Valid @RequestBody CommentUser commentUser, @PathVariable int talkId, HttpServletRequest httpServletRequest)
        throws NotVerifiedException, NotFoundException {
        User user = userService.findById(retrieveUserId(httpServletRequest));
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        TalkUser talk = talkUserService.getOne(user.getId(), talkId);
        CommentUser saved = null;
        if (talk != null) {
            saved = commentService.addComment(user.getId(), talkId, commentUser);

            Locale userPreferredLocale = httpServletRequest.getLocale();
            emailingService.sendNewCommentToAdmins(user, talk, userPreferredLocale);
        }

        return saved;
    }

    /**
     * Edit contact message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CommentUser putContact(@PathVariable int id, @Valid @RequestBody CommentUser commentUser, @PathVariable int talkId, HttpServletRequest req)
        throws NotVerifiedException {
        int userId = retrieveUserId(req);

        commentUser.setId(id);
        return commentService.editComment(userId, talkId, commentUser);
    }

}
