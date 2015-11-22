package fr.sii.controller.restricted;

import com.nimbusds.jwt.JWTClaimsSet;

import javax.servlet.http.HttpServletRequest;

import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.service.auth.AuthUtils;

/**
 * Created by SGUERNIO on 22/11/2015.
 */
public abstract class RestrictedController {

    /**
     * Retrieve user id from request token
     * @param req Request
     * @return User id from token
     * @throws NotVerifiedException If token invalid and user id can't be read
     */
    protected int retrieveUserId(HttpServletRequest req) throws NotVerifiedException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if(claimsSet == null || claimsSet.getClaim("verified") == null || !(boolean)claimsSet.getClaim("verified")) {
            throw new NotVerifiedException("User must be verified");
        }
        return Integer.parseInt(claimsSet.getSubject());
    }
}
