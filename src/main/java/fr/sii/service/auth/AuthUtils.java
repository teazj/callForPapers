package fr.sii.service.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import fr.sii.domain.token.Token;
import org.joda.time.DateTime;

import java.text.ParseException;

/**
 * Created by tmaugin on 15/05/2015.
 */
public final class AuthUtils {

    private static final JWSHeader JWT_HEADER = new JWSHeader(JWSAlgorithm.HS256);
    public static final String AUTH_HEADER_KEY = "Authorization";

    public static String TOKEN_SECRET;

    public static String getSubject(String authHeader) throws ParseException, JOSEException {
        return decodeToken(authHeader).getSubject();
    }

    public static ReadOnlyJWTClaimsSet decodeToken(String authHeader) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(getSerializedToken(authHeader));
        if (signedJWT.verify(new MACVerifier(TOKEN_SECRET))) {
            return signedJWT.getJWTClaimsSet();
        } else {
            throw new JOSEException("Signature verification failed");
        }
    }

    public static Token createToken(String host, String sub, boolean verified) throws JOSEException {
        JWTClaimsSet claim = new JWTClaimsSet();
        claim.setSubject(sub);
        claim.setIssuer(host);
        claim.setCustomClaim("verified", verified);
        claim.setIssueTime(DateTime.now().toDate());
        claim.setExpirationTime(DateTime.now().plusDays(14).toDate());

        JWSSigner signer = new MACSigner(TOKEN_SECRET);
        SignedJWT jwt = new SignedJWT(JWT_HEADER, claim);
        jwt.sign(signer);

        return new Token(jwt.serialize());
    }

    public static String getSerializedToken(String authHeader) {
        return authHeader.split(" ")[1];
    }
}