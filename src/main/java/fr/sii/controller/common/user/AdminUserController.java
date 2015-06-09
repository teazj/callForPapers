package fr.sii.controller.common.user;

import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import fr.sii.domain.admin.user.AdminUserInfo;
import fr.sii.domain.common.Redirect;
import fr.sii.domain.common.Uri;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by tmaugin on 24/04/2015.
 */
@Controller
@RequestMapping(value="/api/adminUser")
public class AdminUserController {

    SpreadsheetSettings spreadsheetSettings;

    public void setSpreadsheetSettings(SpreadsheetSettings spreadsheetSettings) {
        this.spreadsheetSettings = spreadsheetSettings;
    }

    @RequestMapping(value="/currentUser", method= RequestMethod.GET)
    @ResponseBody
    public AdminUserInfo getCurrentUser( HttpServletRequest req, HttpServletResponse resp){
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        if (req.getUserPrincipal() != null) {
            return new AdminUserInfo(userService.createLogoutURL("/"), true, userService.isUserAdmin(), userService.getCurrentUser().getEmail().equals(spreadsheetSettings.getLogin()));
        }
        else
        {
            return new AdminUserInfo(userService.createLogoutURL("/"), false, false, false);
        }
    }

    @RequestMapping(value="/login", method= RequestMethod.POST)
    @ResponseBody
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


    @RequestMapping(value="/logout", method= RequestMethod.POST)
    @ResponseBody
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
