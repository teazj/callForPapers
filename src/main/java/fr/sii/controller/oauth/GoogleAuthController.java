package fr.sii.controller.oauth;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
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
import fr.sii.entity.User;
import fr.sii.service.auth.AuthService;

@RestController
@RequestMapping(value = "/auth/google", produces = "application/json; charset=utf-8")
public class GoogleAuthController {

    private final Logger log = LoggerFactory.getLogger(GoogleAuthController.class);

    private static final String ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";

    // private static final String peopleApiUrl = "https://www.googleapis.com/plus/v1/people/me/openIdConnect";

    @Autowired
    GoogleSettings googleSettings;

    @Autowired
    AuthService authService;

    public void setGoogleSettings(GoogleSettings googleSettings) {
        this.googleSettings = googleSettings;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Log in with Google
     *
     * @param httpServletResponse
     * @param httpServletRequest
     * @param info
     * @return
     * @throws IOException
     * @throws JOSEException
     * @throws ParseException
     */
    @RequestMapping(method = RequestMethod.POST)
    public Token loginGoogle(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestBody Map<String, String> info)
            throws IOException, JOSEException, ParseException {

        Token token = null;

        String client_id = googleSettings.getClientId();
        String client_secret = googleSettings.getClientSecret();

        try {
            TokenResponse response = new AuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(), new GenericUrl(ACCESS_TOKEN_URL),
                    info.get("code")).setRedirectUri(info.get("redirectUri")).setCode(info.get("code")).setGrantType("authorization_code")
                            .setClientAuthentication(new BasicAuthentication(client_id, client_secret)).execute();

            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new JacksonFactory();
            GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(jsonFactory).setTransport(httpTransport)
                    .setClientSecrets(client_id, client_secret).build().setFromTokenResponse(response);

            Plus service = new Plus.Builder(httpTransport, jsonFactory, credential).setApplicationName("Call For Paper").build();

            Person person = service.people().get("me").execute();

            String email = person.getEmails().get(0).getValue();
            String socialProfilImageUrl = person.getImage().getUrl().replace("z=50", "z=360");
            String userId = person.getId();
            token = authService.processUser(httpServletResponse, httpServletRequest, User.Provider.GOOGLE, userId, email, socialProfilImageUrl);
        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                log.warn("Error: " + e.getDetails().getError());
                if (e.getDetails().getErrorDescription() != null) {
                    log.warn(e.getDetails().getErrorDescription());
                }
                if (e.getDetails().getErrorUri() != null) {
                    log.warn(e.getDetails().getErrorUri());
                }
            } else {
                log.warn(e.getMessage());
            }
        }
        return token;
    }
}
