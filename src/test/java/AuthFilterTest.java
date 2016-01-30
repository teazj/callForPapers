import com.jayway.restassured.response.Header;
import com.nimbusds.jose.JOSEException;
import fr.sii.config.filter.AuthFilter;
import fr.sii.domain.token.Token;
import fr.sii.service.auth.AuthUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by tmaugin on 22/05/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuthFilterTest {

    private MockFilterChain chain;
    private MockHttpServletRequest req;
    private MockHttpServletResponse rep;

    private String secretToken = "secretTestsecretTestsecretTestsecretTestsecretTestsecretTestsecretTestsecretTest";

    @Before
    public void setUp() {
        AuthUtils.TOKEN_SECRET = secretToken;
        chain = new MockFilterChain();
        req = new MockHttpServletRequest();
        rep = new MockHttpServletResponse();
    }

    public Header getHeader() {
        Token token = null;
        try {
            token = AuthUtils.createToken("testHost", "1", true);
            token.getToken();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return new Header("Authorization", "Bearer " + token.getToken());
    }

    public Header getHeaderNotVerified() {
        Token token = null;
        try {
            token = AuthUtils.createToken("testHost", "1", false);
            token.getToken();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return new Header("Authorization", "Bearer " + token.getToken());
    }

    @Test
    public void test1_authFilterPass() {
        boolean hadError = false;
        AuthFilter authFilter = new AuthFilter();
        req.addHeader(getHeader().getName(), getHeader().getValue());

        try {
            authFilter.doFilter(req, rep, chain);
        } catch (IOException e) {
            hadError = true;
            e.printStackTrace();
        } catch (ServletException e) {
            hadError = true;
            e.printStackTrace();
        }

        assertEquals(false, hadError);
        assertEquals(200, rep.getStatus());
    }

    @Test
    public void test2_authFilterNoHeader() {
        boolean hadError = false;
        AuthFilter authFilter = new AuthFilter();
        try {
            authFilter.doFilter(req, rep, chain);
        } catch (IOException e) {
            hadError = true;
            e.printStackTrace();
        } catch (ServletException e) {
            hadError = true;
            e.printStackTrace();
        }

        assertEquals(false, hadError);
        assertEquals(401, rep.getStatus());
    }


    @Test
    public void test3_authFilterExpired() {
        boolean hadError = false;
        AuthFilter authFilter = new AuthFilter();
        req.addHeader(getHeader().getName(), getHeader().getValue());
        DateTimeUtils.setCurrentMillisFixed(DateTime.now().plusDays(16).getMillis());

        try {
            authFilter.doFilter(req, rep, chain);
        } catch (IOException e) {
            hadError = true;
            e.printStackTrace();
        } catch (ServletException e) {
            hadError = true;
            e.printStackTrace();
        }

        assertEquals(false, hadError);
        assertEquals(401, rep.getStatus());
    }


    @Test
    public void test4_authFilterBadToken() {
        boolean hadError = false;
        AuthFilter authFilter = new AuthFilter();
        req.addHeader(getHeader().getName(), "odfvjiodfv");

        try {
            authFilter.doFilter(req, rep, chain);
        } catch (IOException e) {
            hadError = true;
            e.printStackTrace();
        } catch (ServletException e) {
            hadError = true;
            e.printStackTrace();
        }

        assertEquals(false, hadError);
        assertEquals(401, rep.getStatus());
    }
}
