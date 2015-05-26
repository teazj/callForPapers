package fr.sii.controller.oauth;

import com.nimbusds.jose.JOSEException;
import fr.sii.config.auth.AuthSettings;
import fr.sii.config.global.GlobalSettings;
import fr.sii.domain.email.Email;
import fr.sii.domain.exception.CustomException;
import fr.sii.domain.recaptcha.ReCaptchaCheckerReponse;
import fr.sii.domain.token.Token;
import fr.sii.domain.user.LoginUser;
import fr.sii.domain.user.User;
import fr.sii.service.auth.AuthUtils;
import fr.sii.service.auth.PasswordService;
import fr.sii.service.email.EmailingService;
import fr.sii.service.recaptcha.ReCaptchaChecker;
import fr.sii.service.user.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by tmaugin on 15/05/2015.
 */
@Controller
@RequestMapping(value="/auth", produces = "application/json; charset=utf-8")
public class AuthController {

    private static Logger logger = Logger.getLogger(AuthController.class.getName());

    public static final String CONFLICT_MSG_EMAIL = "There is already account associated with this email",
            ALREADY_VERIFIED = "This account is already verified",
            BAD_TOKEN = "Bad token",
            NOT_FOUND_MSG = "User not found", LOGING_ERROR_MSG = "Wrong email and/or password",
            UNLINK_ERROR_MSG = "Could not unlink %s account because it is your only sign-in method";

    @Autowired
    private UserService userService;

    @Autowired
    private GlobalSettings globalSettings;

    @Autowired
    private EmailingService emailingService;

    @Autowired
    private AuthSettings authSettings;

    @RequestMapping(value="/login", method=RequestMethod.POST)
    @ResponseBody
    public Token login(HttpServletResponse res,HttpServletRequest req, @RequestBody @Valid LoginUser user) throws JOSEException, IOException {
        User foundUser = userService.findByemail(user.getEmail());
        if (foundUser != null
                && PasswordService.checkPassword(user.getPassword(), foundUser.getPassword())) {
            Token token = AuthUtils.createToken(req.getRemoteHost(), foundUser.getEntityId().toString(), foundUser.isVerified());
            return token;
        }
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.getWriter().write(LOGING_ERROR_MSG);
        res.getWriter().flush();
        res.getWriter().close();
        return null;
    }

    @RequestMapping(value="/signup", method=RequestMethod.POST)
    @ResponseBody
    public Token signup(HttpServletResponse res,HttpServletRequest req, @RequestBody @Valid LoginUser loginUser) throws Exception {
        Token token = null;

        ReCaptchaCheckerReponse rep = ReCaptchaChecker.checkReCaptcha(authSettings.getCaptchaSecret(), loginUser.getCaptcha());
        if (!rep.getSuccess()) {
            throw new CustomException("Bad captcha");
        }

        User foundUser = userService.findByemail(loginUser.getEmail());
        // Verify if user already exists
        if (foundUser != null) {
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                res.getWriter().write(CONFLICT_MSG_EMAIL);
                res.getWriter().flush();
                res.getWriter().close();
                return token;
        }

        // Create new user
        User user = new User();
        // Setup verification token for email validation
        String verifyToken = RandomStringUtils.randomAlphanumeric(100);
        user.setVerifyToken(verifyToken);
        user.setPassword(PasswordService.hashPassword(loginUser.getPassword()));
        user.setEmail(loginUser.getEmail());
        // Save user
        User savedUser = userService.save(user);

        // Send validation email
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("link", globalSettings.getHostname() + "/#/verify?id=" + savedUser.getEntityId() + "&token=" + savedUser.getVerifyToken());
        map.put("hostname", globalSettings.getHostname());
        logger.info(globalSettings.getHostname() + "/#/verify?id=" + savedUser.getEntityId() + "&token=" + savedUser.getVerifyToken());

        Email email = new Email(savedUser.getEmail(),"Confirmation de votre adresse e-mail","verify.html",map);
        emailingService.send(email);

        // Return JWT
        token = AuthUtils.createToken(req.getRemoteHost(), savedUser.getEntityId().toString(), savedUser.isVerified());
        return token;
    }

    @RequestMapping(value="/verify", method=RequestMethod.GET)
    @ResponseBody
    public Token verify(HttpServletResponse res,HttpServletRequest req, @RequestParam("id") String id, @RequestParam("token") String verifyToken) throws JOSEException, IOException {
        Token token = null;

        // Search user
        User foundUser = userService.findById(Long.parseLong(id));
        if (foundUser == null) {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.getWriter().write(NOT_FOUND_MSG);
                res.getWriter().flush();
                res.getWriter().close();
                return token;
        }
        else
        {
            // Verify if account already verified
            if(foundUser.isVerified())
            {
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                res.getWriter().write(ALREADY_VERIFIED);
                res.getWriter().flush();
                res.getWriter().close();
                return token;
            }

            // Verify if token match
            if(foundUser.getVerifyToken().equals(verifyToken))
            {
                foundUser.setVerified(true);
                foundUser.setVerifyToken(null);
                User savedUser = userService.put(foundUser.getEntityId(), foundUser);
                token = AuthUtils.createToken(req.getRemoteHost(), savedUser.getEntityId().toString(), savedUser.isVerified());
                return token;
            }
            else
            {
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                res.getWriter().write(BAD_TOKEN);
                res.getWriter().flush();
                res.getWriter().close();
                return token;
            }
        }
    }

    @RequestMapping(value="/unlink/{provider}", method=RequestMethod.GET)
    @ResponseBody
    public void unlink(HttpServletResponse res,HttpServletRequest req, @PathVariable("provider") String provider) throws JOSEException, IOException, ParseException, NoSuchFieldException, IllegalAccessException {
        String authHeader = req.getHeader(AuthUtils.AUTH_HEADER_KEY);

        if (StringUtils.isBlank(authHeader)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(NOT_FOUND_MSG);
            res.getWriter().flush();
            res.getWriter().close();
            return;
        }

        String subject = AuthUtils.getSubject(authHeader);
        User foundUser = userService.findById(Long.parseLong(subject));

        if (foundUser == null) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write(NOT_FOUND_MSG);
            res.getWriter().flush();
            res.getWriter().close();
            return;
        }

        User userToUnlink = foundUser;

        // check that the user is not trying to unlink the only sign-in method
        if (userToUnlink.getSignInMethodCount() == 1) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write(String.format(UNLINK_ERROR_MSG, provider));
            res.getWriter().flush();
            res.getWriter().close();
            return;
        }

        try {
            userToUnlink.setProviderId(User.Provider.valueOf(provider.toUpperCase()), null);
        } catch (final IllegalArgumentException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        userService.put(userToUnlink.getEntityId(), userToUnlink);
        return;
    }
}
