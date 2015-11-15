package fr.sii.controller.oauth;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.nimbusds.jose.JOSEException;
import fr.sii.config.github.GithubSettings;
import fr.sii.domain.exception.CustomException;
import fr.sii.domain.token.Token;
import fr.sii.entity.User;
import fr.sii.service.auth.AuthService;
import fr.sii.service.github.GithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping(value="/auth/github", produces = "application/json; charset=utf-8")
public class GithubAuthController {

    @Autowired
    GithubService githubService;

    @Autowired
    GithubSettings githubSettings;

    @Autowired
    AuthService authService;

    /**
     * Log in with Github
     * @param res
     * @param req
     * @param info
     * @return
     * @throws IOException
     * @throws CustomException
     * @throws JOSEException
     * @throws ParseException
     */
    @RequestMapping(method= RequestMethod.POST)
    public Token doGet(HttpServletResponse res, HttpServletRequest req, @RequestBody Map<String,String> info)
            throws IOException, CustomException, JOSEException, ParseException {
        String accessTokenUrl = "https://github.com/login/oauth/access_token";
        String peopleApiUrl = "https://api.github.com/user";
        String client_id = githubSettings.getClientId();
        String client_secret = githubSettings.getClientSecret();

        Token token = null;

        try {
            TokenResponse response =
                    new AuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            new JacksonFactory(),
                            new GenericUrl(accessTokenUrl),
                            info.get("code"))
                            .setRequestInitializer(
                                    new HttpRequestInitializer() {
                                        public void initialize(HttpRequest request) {
                                            request.setHeaders(new HttpHeaders().setAccept("application/json"));
                                        }
                                    }
                            )
                            .setClientAuthentication(
                                    new ClientParametersAuthentication(client_id, client_secret))
                            .execute();

            String email = githubService.getEmail(response.getAccessToken());
            String userId = githubService.getUserId(response.getAccessToken());
            String socialProfilImageUrl = githubService.getAvatarUrl(response.getAccessToken());
            token = authService.processUser(res, req, User.Provider.GITHUB, userId, email, socialProfilImageUrl);

        } catch (TokenResponseException e) {
            e.printStackTrace();
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