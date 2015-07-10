package fr.sii.controller.oauth;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.nimbusds.jose.JOSEException;
import fr.sii.config.google.GoogleSettings;
import fr.sii.domain.token.Token;
import fr.sii.domain.user.User;
import fr.sii.service.auth.AuthService;
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
 * Created by tmaugin on 13/05/2015.
 */
@Controller
@RequestMapping(value="/auth/google", produces = "application/json; charset=utf-8")
public class GoogleAuthController {

    GoogleSettings googleSettings;

    AuthService authService;

    public void setGoogleSettings(GoogleSettings googleSettings) {
        this.googleSettings = googleSettings;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(method= RequestMethod.POST)
    @ResponseBody
    public Token loginGoogle(HttpServletResponse res, HttpServletRequest req, @RequestBody Map<String,String> info)
            throws IOException, JOSEException, ParseException {

        Token token = null;

        String accessTokenUrl = "https://accounts.google.com/o/oauth2/token";
        String peopleApiUrl = "https://www.googleapis.com/plus/v1/people/me/openIdConnect";
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

            HttpTransport httpTransport = new UrlFetchTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory(jsonFactory)
                    .setTransport(httpTransport)
                    .setClientSecrets(client_id, client_secret).build()
                    .setFromTokenResponse(response);

            Plus service = new Plus.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName("Call For Paper")
                    .build();

            Person profile = service.people().get("me").execute();

            String email = profile.getEmails().get(0).getValue();
            String socialProfilImageUrl = profile.getImage().getUrl().replace("z=50", "z=360");
            String userId = profile.getId();
            token = authService.processUser(res, req, User.Provider.GOOGLE, userId, email, socialProfilImageUrl);
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
        }
        return token;
    }
}