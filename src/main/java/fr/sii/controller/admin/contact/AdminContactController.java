package fr.sii.controller.admin.contact;

import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.email.Email;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.dto.CommentUser;
import fr.sii.dto.TalkAdmin;
import fr.sii.entity.AdminUser;
import fr.sii.entity.User;
import fr.sii.service.CommentAdminService;
import fr.sii.service.TalkAdminService;
import fr.sii.service.admin.contact.AdminContactService;
import fr.sii.service.admin.user.AdminUserService;
import fr.sii.service.email.EmailingService;
import fr.sii.service.spreadsheet.SpreadsheetService;
import fr.sii.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="api/admin/sessions/{talkId}/contacts", produces = "application/json; charset=utf-8")
public class AdminContactController {

    private AdminContactService adminContactService;

    private AdminUserService adminUserServiceCustom;

    private SpreadsheetService googleService;

    private GlobalSettings globalSettings;

    private EmailingService emailingService;

    @Autowired
    private CommentAdminService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TalkAdminService talkService;

    public void setAdminContactService(AdminContactService adminContactService) {
        this.adminContactService = adminContactService;
    }

    public void setAdminUserServiceCustom(AdminUserService adminUserServiceCustom) {
        this.adminUserServiceCustom = adminUserServiceCustom;
    }

    public void setGoogleService(SpreadsheetService googleService) {

        this.googleService = googleService;
    }

    public void setGlobalSettings(GlobalSettings globalSettings) {

        this.globalSettings = globalSettings;
    }

    public void setEmailingService(EmailingService emailingService) {

        this.emailingService = emailingService;
    }

    /**
     * Get all contact message for a given session
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<CommentUser> getAll(@PathVariable int talkId) throws NotVerifiedException, NotFoundException, ForbiddenException, IOException {
        return commentService.findAll(talkId);
    }

    /**
     * Add new contact message to a session
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public CommentUser postContact(@Valid @RequestBody CommentUser comment, @PathVariable int talkId) throws NotFoundException, IOException {
        AdminUser admin = adminUserServiceCustom.getCurrentUser();
        TalkAdmin talk = talkService.getOne(talkId);
        User u = userService.findById(talk.getUserId());

        CommentUser saved = commentService.addComment(admin, talkId, comment);

        try {
            List<String> bcc = new ArrayList<>();
            for(AdminUser adminUser : adminUserServiceCustom.findAll()) {
                bcc.add(adminUser.getEmail());
            }
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", u.getFirstname());
            map.put("talk", talk.getName());
            map.put("hostname", globalSettings.getHostname());
            map.put("id", String.valueOf(talkId));

            Email email = new Email(u.getEmail(), "Vous avez un nouveau message à propos du talk " + talk.getName(), "newMessage.html", map, bcc);
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
    @ResponseBody public CommentUser putContact(@PathVariable int id, @Valid @RequestBody CommentUser comment) throws NotFoundException, ForbiddenException {
        comment.setId(id);
        return commentService.editComment(comment);
    }
}