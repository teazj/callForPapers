package fr.sii.controller.restricted.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.sii.config.global.GlobalSettings;
import fr.sii.controller.restricted.RestrictedController;
import fr.sii.domain.email.Email;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.dto.CommentUser;
import fr.sii.dto.TalkUser;
import fr.sii.entity.AdminUser;
import fr.sii.entity.User;
import fr.sii.service.CommentUserService;
import fr.sii.service.TalkUserService;
import fr.sii.service.admin.user.AdminUserService;
import fr.sii.service.email.EmailingService;
import fr.sii.service.user.UserService;


@RestController
@RequestMapping(value="api/restricted/sessions/{talkId}/contacts", produces = "application/json; charset=utf-8")
public class ContactController extends RestrictedController {


    @Autowired
    private AdminUserService adminUserServiceCustom;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailingService emailingService;

    @Autowired
    private GlobalSettings globalSettings;

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
    public CommentUser postContact(@Valid @RequestBody CommentUser commentUser, @PathVariable int talkId, HttpServletRequest req) throws NotVerifiedException, NotFoundException {
        User u = userService.findById(retrieveUserId(req));
        if(u == null) throw new NotFoundException("User not found");
        TalkUser talk = talkUserService.getOne(u.getId(), talkId);

        CommentUser saved = commentService.addComment(u.getId(), talkId, commentUser);

        try {
            List<String> bcc = new ArrayList<>();
            for(AdminUser adminUser : adminUserServiceCustom.findAll()) {
                bcc.add(adminUser.getEmail());
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("name", u.getFirstname());
            map.put("talk", talk.getName());
            map.put("hostname", globalSettings.getHostname());
            map.put("id", String.valueOf(talkId));

            Email email = new Email(null,"Le speaker " + u.getFirstname() + " " + u.getLastname() + " à écrit un message à propos du talk" + talk.getName(),"newMessageAdmin.html",map, bcc);
            emailingService.send(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saved;
    }

    /**
     * Edit contact message
     */
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public CommentUser putContact(@PathVariable int id, @Valid @RequestBody CommentUser commentUser, @PathVariable int talkId, HttpServletRequest req) throws NotVerifiedException {
        int userId = retrieveUserId(req);

        commentUser.setId(id);
        return commentService.editComment(userId, talkId, commentUser);
    }


}
