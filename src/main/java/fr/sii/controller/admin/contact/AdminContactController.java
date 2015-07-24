package fr.sii.controller.admin.contact;

import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.admin.contact.AdminContact;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.email.Email;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.service.admin.contact.AdminContactService;
import fr.sii.service.admin.user.AdminUserService;
import fr.sii.service.email.EmailingService;
import fr.sii.service.spreadsheet.SpreadsheetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tmaugin on 24/07/2015.
 */
@Controller
@RequestMapping(value="api/admin/contacts", produces = "application/json; charset=utf-8")
public class AdminContactController {

    private AdminContactService adminContactService;

    private AdminUserService adminUserServiceCustom;

    private SpreadsheetService googleService;

    private GlobalSettings globalSettings;

    private EmailingService emailingService;

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


    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public AdminContact postContact(@Valid @RequestBody AdminContact adminContact) {
        adminContact.setUserId(adminUserServiceCustom.getCurrentUser().getEntityId());
        adminContact.setAdmin(true);
        AdminContact postedAdminContact =  adminContactService.save(adminContact);
        try {
            List<String> bcc = new ArrayList<>();
            for(AdminUser adminUSer : adminUserServiceCustom.findAll()) {
                bcc.add(adminUSer.getEmail());
            }
            Row row = googleService.getRow(postedAdminContact.getRowId().toString());
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", row.getFirstname());
            map.put("talk", row.getSessionName());
            map.put("hostname", globalSettings.getHostname());
            map.put("id", row.getAdded().toString());

            Email email = new Email(row.getEmail(),"Vous avez un nouveau message à propos du talk " + row.getSessionName(),"newMessage.html",map, bcc);
            emailingService.send(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postedAdminContact;
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseBody public AdminContact putContact(@PathVariable Long id, @Valid @RequestBody AdminContact adminContact) throws NotFoundException, ForbiddenException {
        AdminContact currentComment = adminContactService.findOne(id);
        if(currentComment.isAdmin() && currentComment.getUserId().toString().equals(adminUserServiceCustom.getCurrentUser().getEntityId().toString())) {
            adminContact.setUserId(adminUserServiceCustom.getCurrentUser().getEntityId());
            adminContact.setAdded(new Date(currentComment.getAdded()));
            adminContact.setAdmin(true);
            return adminContactService.put(id, adminContact);
        } else {
            throw new ForbiddenException("This is not your contact, you can't edit it");
        }
    }

    @RequestMapping(value="/row/{rowId}", method= RequestMethod.GET)
    @ResponseBody
    public List<AdminContact> getContactsByRowId(@PathVariable Long rowId ) {
        return adminContactService.findByRowId(rowId);
    }
}