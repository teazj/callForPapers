package fr.sii.controller.admin.contact;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
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
import java.io.IOException;
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


    /**
     * Add new contact message to a session
     * @param adminContact
     * @return
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public AdminContact postContact(@Valid @RequestBody AdminContact adminContact) throws ServiceException, EntityNotFoundException, NotFoundException, IOException {
        adminContact.setUserId(adminUserServiceCustom.getCurrentUser().getEntityId());
        adminContact.setAdmin(true);
        AdminContact postedAdminContact =  adminContactService.save(adminContact);
        // Verify user allowed => Throw forbidden exception;
        Row row = googleService.getRow(postedAdminContact.getRowId().toString());
        try {
            List<String> bcc = new ArrayList<>();
            for(AdminUser adminUser : adminUserServiceCustom.findAll()) {
                bcc.add(adminUser.getEmail());
            }
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

    /**
     * Edit contact message
     * @param id
     * @param adminContact
     * @return
     * @throws NotFoundException
     * @throws ForbiddenException
     */
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

    /**
     * Get all contact messages for a specific session
     * @param rowId
     * @return
     */
    @RequestMapping(value="/row/{rowId}", method= RequestMethod.GET)
    @ResponseBody
    public List<AdminContact> getContactsByRowId(@PathVariable Long rowId ) {
        return adminContactService.findByRowId(rowId);
    }
}