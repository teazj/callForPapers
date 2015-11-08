package fr.sii.controller.restricted.contact;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.gdata.util.ServiceException;
import com.nimbusds.jwt.JWTClaimsSet;
import fr.sii.config.auth.AuthSettings;
import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.admin.contact.AdminContact;
import fr.sii.domain.admin.user.AdminUser;
import fr.sii.domain.email.Email;
import fr.sii.domain.exception.*;
import fr.sii.domain.recaptcha.ReCaptchaCheckerReponse;
import fr.sii.domain.spreadsheet.Row;
import fr.sii.entity.User;
import fr.sii.service.admin.contact.AdminContactService;
import fr.sii.service.admin.user.AdminUserService;
import fr.sii.service.auth.AuthUtils;
import fr.sii.service.email.EmailingService;
import fr.sii.service.recaptcha.ReCaptchaChecker;
import fr.sii.service.spreadsheet.SpreadsheetService;
import fr.sii.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping(value="api/restricted/contacts", produces = "application/json; charset=utf-8")
public class ContactController {

    private AdminContactService adminContactService;

    private SpreadsheetService googleService;

    private AdminUserService adminUserServiceCustom;

    private UserService userService;

    private EmailingService emailingService;

    private GlobalSettings globalSettings;

    private AuthSettings authSettings;

    public void setAuthSettings(AuthSettings authSettings) {
        this.authSettings = authSettings;
    }


    public void setGlobalSettings(GlobalSettings globalSettings) {

        this.globalSettings = globalSettings;
    }

    public void setAdminContactService(AdminContactService adminContactService) {
        this.adminContactService = adminContactService;
    }

    public void setUserService(UserService userService) {

        this.userService = userService;
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

    /**
     * Add a contact message for the given session
     * @param adminContact
     * @param req
     * @return
     * @throws CustomException
     * @throws ServiceException
     * @throws EntityNotFoundException
     * @throws IOException
     */
    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody public AdminContact postContact(@Valid @RequestBody AdminContact adminContact, HttpServletRequest req) throws CustomException, ServiceException, EntityNotFoundException, IOException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }

        User u = userService.findById(Integer.parseInt(claimsSet.getSubject()));
        if(u == null)
        {
            throw new NotFoundException("User not found");
        }

        if(adminContact.getCaptcha() == null) {
            throw new BadRequestException("Captcha field is required");
        } else {
            ReCaptchaCheckerReponse rep = ReCaptchaChecker.checkReCaptcha(authSettings.getCaptchaSecret(), adminContact.getCaptcha());
            if (!rep.getSuccess()) {
                throw new BadRequestException("Bad captcha");
            }
        }

        // Verify user allowed => Throw forbidden exception;
        Row row = googleService.getRow(adminContact.getRowId().toString(), u.getEntityId());

        adminContact.setUserId(u.getEntityId());
        adminContact.setAdmin(false);
        AdminContact postedAdminContact =  adminContactService.save(adminContact);

        try {
            List<String> bcc = new ArrayList<>();
            for(AdminUser adminUSer : adminUserServiceCustom.findAll()) {
                bcc.add(adminUSer.getEmail());
            }
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", row.getFirstname()+ " " + row.getName() );
            map.put("talk", row.getSessionName());
            map.put("hostname", globalSettings.getHostname());
            map.put("id", row.getAdded().toString());

            Email email = new Email(null,"Le speaker " + row.getFirstname()+ " " + row.getName() + " à écrit un message à propos du talk" + row.getSessionName(),"newMessageAdmin.html",map, bcc);
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
     * @param req
     * @return
     * @throws NotFoundException
     * @throws ForbiddenException
     * @throws NotVerifiedException
     * @throws ServiceException
     * @throws EntityNotFoundException
     * @throws IOException
     */
    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseBody public AdminContact putContact(@PathVariable Long id, @Valid @RequestBody AdminContact adminContact, HttpServletRequest req) throws NotFoundException, ForbiddenException, NotVerifiedException, ServiceException, EntityNotFoundException, IOException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }

        User u = userService.findById(Integer.parseInt(claimsSet.getSubject()));
        if(u == null)
        {
            throw new NotFoundException("User not found");
        }

        AdminContact currentContact = adminContactService.findOne(id);
        // Verify user allowed => Throw forbidden exception;
        googleService.getRow(currentContact.getRowId().toString(), u.getEntityId());
        if(!currentContact.isAdmin() && currentContact.getUserId().toString().equals(u.getEntityId().toString())) {
            adminContact.setUserId(u.getEntityId());
            adminContact.setAdded(new Date(currentContact.getAdded()));
            adminContact.setAdmin(false);
            return adminContactService.put(id, adminContact);
        } else {
            throw new ForbiddenException("This is not your contact, you can't edit it");
        }
    }

    /**
     * Get all contact message for a given session
     * @param rowId
     * @param req
     * @return
     * @throws NotVerifiedException
     * @throws NotFoundException
     * @throws ServiceException
     * @throws ForbiddenException
     * @throws EntityNotFoundException
     * @throws IOException
     */
    @RequestMapping(value="/row/{rowId}", method= RequestMethod.GET)
    @ResponseBody
    public List<AdminContact> getContactsByRowId(@PathVariable Long rowId, HttpServletRequest req) throws NotVerifiedException, NotFoundException, ServiceException, ForbiddenException, EntityNotFoundException, IOException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified"))
        {
            throw new NotVerifiedException("User must be verified");
        }

        User u = userService.findById(Integer.parseInt(claimsSet.getSubject()));
        if(u == null)
        {
            throw new NotFoundException("User not found");
        }
        // Verify user allowed => Throw forbidden exception;
        googleService.getRow(rowId.toString(), u.getEntityId());

        return adminContactService.findByRowId(rowId);
    }
}