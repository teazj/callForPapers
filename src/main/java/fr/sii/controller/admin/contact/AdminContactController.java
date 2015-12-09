package fr.sii.controller.admin.contact;

import java.io.IOException;
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

import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.dto.CommentUser;
import fr.sii.dto.TalkAdmin;
import fr.sii.entity.AdminUser;
import fr.sii.entity.User;
import fr.sii.service.CommentAdminService;
import fr.sii.service.TalkAdminService;
import fr.sii.service.admin.user.AdminUserService;
import fr.sii.service.email.EmailingService;
import fr.sii.service.user.UserService;

@RestController
@RequestMapping(value = "api/admin/sessions/{talkId}/contacts", produces = "application/json; charset=utf-8")
public class AdminContactController {

    @Autowired
    private AdminUserService adminUserServiceCustom;

    @Autowired
    private EmailingService emailingService;

    @Autowired
    private CommentAdminService commentService;

    @Autowired
    private TalkAdminService talkService;

    @Autowired
    private UserService userService;

    /**
     * Get all contact message for a given session
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<CommentUser> getAll(@PathVariable int talkId) {
        return commentService.findAll(talkId, false);
    }

    /**
     * Add new contact message to a session
     */
    @RequestMapping(method = RequestMethod.POST)
    public CommentUser postContact(@Valid @RequestBody CommentUser comment, @PathVariable int talkId, HttpServletRequest httpServletRequest) throws NotFoundException, IOException {

        AdminUser admin = adminUserServiceCustom.getCurrentUser();
        TalkAdmin talk = talkService.getOne(talkId);
        User user = userService.findById(talk.getUserId());

        CommentUser saved = commentService.addComment(admin, talkId, comment, false);

        // Send new message email
        if (user != null) {
            Locale userPreferredLocale = httpServletRequest.getLocale();
            emailingService.sendNewMessage(user, talk, userPreferredLocale);
        }

        return saved;
    }

    /**
     * Edit contact message
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CommentUser putContact(@PathVariable int id, @Valid @RequestBody CommentUser comment) throws NotFoundException, ForbiddenException {
        comment.setId(id);
        return commentService.editComment(comment);
    }
}
