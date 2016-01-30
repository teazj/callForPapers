package fr.sii.controller.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.nimbusds.jose.JOSEException;
import fr.sii.config.github.GithubSettings;
import fr.sii.domain.exception.CustomException;
import fr.sii.domain.token.Token;
import fr.sii.entity.User;
import fr.sii.service.github.GithubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth/github", produces = "application/json; charset=utf-8")
public class GithubAuthController extends OAuthController {

    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_USER_URL = "https://api.github.com/user";
    private static final String GITHUB_USER_EMAILS_URL = "https://api.github.com/user/emails";
    private final Logger log = LoggerFactory.getLogger(GoogleAuthController.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    GithubService githubService;

    @Autowired
    GithubSettings githubSettings;

    @Autowired
    OAuthController OAuthController;

    /**
     * Log in with Github
     *
     * @param httpServletResponse
     * @param httpServletRequest
     * @param info
     * @return
     * @throws IOException
     * @throws CustomException
     * @throws JOSEException
     * @throws ParseException
     */
    @RequestMapping(method = RequestMethod.POST)
    public Token loginGithub(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestBody Map<String, String> info)
        throws IOException, CustomException, JOSEException {

        String client_id = githubSettings.getClientId();
        String client_secret = githubSettings.getClientSecret();

        Token token = null;

        try {
            TokenResponse tokenResponse = new AuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(), new GenericUrl(ACCESS_TOKEN_URL),
                info.get("code")).setRequestInitializer(request -> request.setHeaders(new HttpHeaders().setAccept("application/json")))
                .setClientAuthentication(new ClientParametersAuthentication(client_id, client_secret)).execute();

            GithubUser gitHubUser = getGithubUser(tokenResponse.getAccessToken());
            User userFromGitHub = new User();
            userFromGitHub.setFirstname(StringUtils.split(gitHubUser.getName(), " ")[0]);
            userFromGitHub.setLastname(StringUtils.split(gitHubUser.getName(), " ")[1]);
            userFromGitHub.setEmail(gitHubUser.getEmail());
            userFromGitHub.setProviderId(User.Provider.GITHUB, "" + gitHubUser.getId());
            userFromGitHub.setGithub(gitHubUser.getUrl());
            userFromGitHub.setBio(gitHubUser.getBio());
            userFromGitHub.setCompany(gitHubUser.getCompany());
            userFromGitHub.setImageProfilURL(gitHubUser.getAvatarUrl());

            String userId = "" + gitHubUser.getId();

            token = OAuthController.processUser(httpServletResponse, httpServletRequest, User.Provider.GITHUB, userId, userFromGitHub);
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

    /**
     * Get GitHub user profile
     *
     * @param access_token
     * @return
     * @throws IOException
     * @throws CustomException
     */
    public GithubUser getGithubUser(String access_token) throws IOException, CustomException {
        String jsonString = getJson(access_token, GITHUB_USER_URL);
        GithubUser gitHubUser = mapper.readValue(jsonString, GithubUser.class);

        // set primary email
        gitHubUser.setEmail(getEmail(access_token));

        return gitHubUser;
    }

    /**
     * Get account email using access token and Github API
     *
     * @param access_token
     * @return Email
     * @throws IOException
     * @throws CustomException
     */
    private String getEmail(String access_token) throws IOException, CustomException {
        String jsonString = getJson(access_token, GITHUB_USER_EMAILS_URL);
        JsonNode responseObject = mapper.readTree(jsonString);

        if (null != responseObject.get("message")) {
            throw new CustomException(responseObject.get("message").asText());
        }
        String email = "";
        if (responseObject.get("message") == null) {
            for (JsonNode node : responseObject) {
                if (node.get("primary").asBoolean()) {
                    email = node.get("email").asText();
                    break;
                }
            }
        }
        return email;
    }

    /**
     * Get Github JSON
     *
     * @param access_token
     * @param url
     * @return
     * @throws IOException
     * @throws CustomException
     */
    private String getJson(String access_token, String url) throws IOException, CustomException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + access_token);
        connection.setRequestProperty("User-Agent", "Call For Paper");

        String jsonString = "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                jsonString += inputLine;
            }
        }

        return jsonString.replaceAll("\r", "").replaceAll("\n", "");
    }

}
