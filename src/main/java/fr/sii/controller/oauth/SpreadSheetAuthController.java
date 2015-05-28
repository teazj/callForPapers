package fr.sii.controller.oauth;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserServiceFactory;
import com.nimbusds.jose.JOSEException;
import fr.sii.config.google.GoogleSettings;
import fr.sii.config.spreadsheet.SpreadsheetSettings;
import fr.sii.domain.exception.ForbiddenException;
import fr.sii.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by tmaugin on 27/05/2015.
 * SII
 */

@Controller
@RequestMapping(value="/auth/spreadsheet", produces = "application/json; charset=utf-8")
public class SpreadSheetAuthController {

    @Autowired
    GoogleSettings googleSettings;

    @Autowired
    SpreadsheetSettings spreadsheetSettings;

    @Autowired
    fr.sii.service.spreadsheet.SpreadsheetService spreadsheetService;

    @Autowired
    AuthService authService;

    @RequestMapping(method= RequestMethod.POST)
    @ResponseBody
    public boolean loginSpreadhseet(HttpServletResponse res, HttpServletRequest req, @RequestBody Map<String,String> info)
            throws IOException, JOSEException, ParseException, ForbiddenException {

        com.google.appengine.api.users.UserService userService = UserServiceFactory.getUserService();
        com.google.appengine.api.users.User user = userService.getCurrentUser();

        if(user == null) {
            throw new ForbiddenException("You are not autorized to do that");
        }
        if(!user.getEmail().equals(spreadsheetSettings.getLogin())){
            throw new ForbiddenException("You are not autorized to do that");
        }
        boolean result= false;

        String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";
        String client_id = googleSettings.getClientId();
        String client_secret = googleSettings.getClientSecret();

        try {
            TokenResponse response =
                    new AuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            new JacksonFactory(),
                            new GenericUrl(accessTokenUrl), info.get("code"))
                            .setRedirectUri(info.get("redirectUri"))
                            .setCode(info.get("code"))
                            .setGrantType("authorization_code")
                            .setClientAuthentication(
                                    new BasicAuthentication(client_id, client_secret))
                            .execute();

            if(response.getRefreshToken() != null && !response.getRefreshToken().equals(""))
            {
                spreadsheetService.login(response.getRefreshToken());

                Key spreadsheetTokenKey = KeyFactory.createKey("Token", "Spreadsheet");
                Entity spreadsheetToken = new Entity(spreadsheetTokenKey);

                spreadsheetToken.setProperty("refresh_token", response.getRefreshToken());
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                datastore.put(spreadsheetToken);
                result = true;
            }
        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                System.err.println("Error: " + e.getDetails().getError());
                if (e.getDetails().getErrorDescription() != null) {
                    System.err.println(e.getDetails().getErrorDescription());
                }
                if (e.getDetails().getErrorUri() != null) {
                    System.err.println(e.getDetails().getErrorUri());
                }
            } else {
                System.err.println(e.getMessage());
            }
            throw new ForbiddenException("You are not autorized to do that");
        }
        return true;
    }
}