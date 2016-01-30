package fr.sii.controller.restricted;

import com.nimbusds.jwt.JWTClaimsSet;
import fr.sii.domain.exception.NotVerifiedException;
import fr.sii.service.auth.AuthUtils;

import javax.servlet.http.HttpServletRequest;

public abstract class RestrictedController {

    /**
     * Retrieve user id from request token
     *
     * @param req Request
     * @return User id from token
     * @throws NotVerifiedException If token invalid and user id can't be read
     */
    protected int retrieveUserId(HttpServletRequest req) throws NotVerifiedException {
        JWTClaimsSet claimsSet = AuthUtils.getTokenBody(req);
        if (claimsSet == null) {
            throw new NotVerifiedException("Claims Set is null");
        }

        if (claimsSet.getClaim("verified") == null || !(boolean) claimsSet.getClaim("verified")) {
            throw new NotVerifiedException("User [" + claimsSet.getSubject() + "] must be verified");
        }
        return Integer.parseInt(claimsSet.getSubject());
    }
}
