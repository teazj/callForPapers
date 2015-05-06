package fr.sii.controller.application;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.config.application.ApplicationSettings;
import fr.sii.domain.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tmaugin on 30/04/2015.
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
    public UserInfo getCurrentUser(HttpServletRequest req, HttpServletResponse resp){
        UserService userService = UserServiceFactory.getUserService();
        if (req.getUserPrincipal() != null) {
            return new UserInfo(true, userService.isUserAdmin(),userService.createLogoutURL("/"));
        }
        else
        {
            return new UserInfo(false, false,userService.createLoginURL("/#/admin"));
        }
    }
}