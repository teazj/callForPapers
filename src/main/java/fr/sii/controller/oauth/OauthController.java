package fr.sii.controller.oauth;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OauthController {

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        String thisURL = req.getRequestURI();
        resp.setContentType("text/html");
        if (req.getUserPrincipal() != null) {
            resp.getWriter().println("<p>Hello, " +
                    req.getUserPrincipal().getName() +
                    "!  You can <a href=\"" +
                    userService.createLogoutURL(thisURL) +
                    "\">sign out</a>.</p>");
        } else {
            resp.getWriter().println("<p>Please <a href=\"" +
                    userService.createLoginURL(thisURL) +
                    "\">sign in</a>.</p>");
        }
    }
}