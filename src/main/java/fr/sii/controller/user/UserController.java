package fr.sii.controller.user;

import com.google.appengine.api.users.UserServiceFactory;
import fr.sii.domain.common.Redirect;
import fr.sii.domain.common.Uri;
import fr.sii.domain.user.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by tmaugin on 24/04/2015.
 */
@Controller
@RequestMapping(value="/user")
public class UserController {

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
