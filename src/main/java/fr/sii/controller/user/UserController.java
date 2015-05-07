package fr.sii.controller.user;

import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.config.application.ApplicationSettings;
import fr.sii.domain.application.Login;
import fr.sii.domain.application.UserInfo;
import fr.sii.domain.user.User;
import fr.sii.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by tmaugin on 24/04/2015.
 */
@Controller
@RequestMapping(value="/application")
public class ApplicationController {

    @Autowired
    ApplicationSettings applicationSettings;

    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public ApplicationSettings getApplicationSettings() {
        return applicationSettings;
    }

    @RequestMapping(value="/currentUser", method= RequestMethod.GET)
    @ResponseBody
    public UserInfo getCurrentUser( HttpServletRequest req, HttpServletResponse resp){
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        if (req.getUserPrincipal() != null) {
            return new UserInfo(true, userService.isUserAdmin(),userService.createLogoutURL("/"));
        }
        else
        {
            return new UserInfo(false, false,userService.createLoginURL("/"));
        }
    }

    @RequestMapping(value="/currentUser/login", method= RequestMethod.POST)
    @ResponseBody
    public Login postLogin(@RequestBody String redirect, HttpServletRequest req, HttpServletResponse resp){
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        if (req.getUserPrincipal() != null) {
            return new Login(null);
        }
        else
        {
            return new Login(userService.createLoginURL(redirect));
        }
    }


    @RequestMapping(value="/currentUser/logout", method= RequestMethod.POST)
    @ResponseBody
    public Login postLogout(@RequestBody String redirect, HttpServletRequest req, HttpServletResponse resp){
        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        if (req.getUserPrincipal() != null) {
            return new Login(userService.createLogoutURL(redirect));
        }
        else
        {
            return new Login(null);
        }
    }
}
