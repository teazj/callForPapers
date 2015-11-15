package fr.sii.controller.common.user;

import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import fr.sii.domain.common.Redirect;
import fr.sii.domain.common.Uri;
import fr.sii.dto.AdminUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by tmaugin on 24/04/2015.
 */
@RestController
@RequestMapping(value="/api/adminUser")
public class AdminUserController {

    @Autowired
    private SpreadsheetSettings spreadsheetSettings;


    /**
     * Obtain current admin user information
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/currentUser", method= RequestMethod.GET)
    public AdminUserInfo getCurrentUser( HttpServletRequest req, HttpServletResponse resp){
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        if (req.getUserPrincipal() != null) {
            return new AdminUserInfo(userService.createLogoutURL("/"), true, userService.isUserAdmin(), userService.getCurrentUser().getEmail().equals(spreadsheetSettings.getLogin()), userService.getCurrentUser().getEmail());
        }
        else
        {
            return new AdminUserInfo(userService.createLogoutURL("/"), false, false, false, "");
        }
    }

    /**
     * Obtain login URL
     * @param redirect
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/login", method= RequestMethod.POST)
    public Uri postLogin(@RequestBody @Valid Redirect redirect, HttpServletRequest req, HttpServletResponse resp){
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        if (req.getUserPrincipal() != null) {
            return new Uri(null);
        }
        else
        {
            return new Uri(userService.createLoginURL(redirect.getRedirect()));
        }
    }

    /**
     * Obtain logout URL
     * @param redirect
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value="/logout", method= RequestMethod.POST)
    public Uri postLogout(@RequestBody @Valid Redirect redirect, HttpServletRequest req, HttpServletResponse resp){
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        if (req.getUserPrincipal() != null) {
            return new Uri(userService.createLogoutURL(redirect.getRedirect()));
        }
        else
        {
            return new Uri(null);
        }
    }
}
