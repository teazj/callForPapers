/**
 * Created by tmaugin on 22/05/2015.
 */

import fr.sii.config.filter.CsrfFilter;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CsrfFilterTest {

    private MockFilterChain chain;
    private MockHttpServletRequest req;
    private MockHttpServletResponse rep;

    @Before
    public void setUp() {
        chain = new MockFilterChain();
        req = new MockHttpServletRequest();
        rep = new MockHttpServletResponse();
    }

    @Test
    public void test1_csrfFilterPass() {
        boolean hadError = false;
        CsrfFilter csrfFilter = new CsrfFilter();
        req.addHeader("X-CSRF-TOKEN", "sf927dfsg84665fdg45df");
        req.setCookies(new Cookie("CSRF-TOKEN", "sf927dfsg84665fdg45df"));
        try {
            csrfFilter.doFilter(req, rep, chain);
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
    public void test2_csrfFilterMissMatch() {
        boolean hadError = false;
        CsrfFilter csrfFilter = new CsrfFilter();
        req.addHeader("X-CSRF-TOKEN", "65fdg45df");
        req.setCookies(new Cookie("CSRF-TOKEN", "sf927dfsg84665fdg45df"));
        try {
            csrfFilter.doFilter(req, rep, chain);
        } catch (IOException e) {
            hadError = true;
            e.printStackTrace();
        } catch (ServletException e) {
            hadError = true;
            e.printStackTrace();
        }

        assertEquals(false, hadError);
        assertEquals(403, rep.getStatus());
    }

    @Test
    public void test3_csrfFilterMissMatch2() {
        boolean hadError = false;
        CsrfFilter csrfFilter = new CsrfFilter();
        req.addHeader("X-CSRF-TOKEN", "sf927dfsg84665fdg45df");
        req.setCookies(new Cookie("CSRF-TOKEN", "84665fdg45df"));
        try {
            csrfFilter.doFilter(req, rep, chain);
        } catch (IOException e) {
            hadError = true;
            e.printStackTrace();
        } catch (ServletException e) {
            hadError = true;
            e.printStackTrace();
        }

        assertEquals(false, hadError);
        assertEquals(403, rep.getStatus());
    }

    @Test
    public void test4_csrfFilterNoHeader() {
        boolean hadError = false;
        CsrfFilter csrfFilter = new CsrfFilter();
        req.setCookies(new Cookie("CSRF-TOKEN", "sf927dfsg84665fdg45df"));
        try {
            csrfFilter.doFilter(req, rep, chain);
        } catch (IOException e) {
            hadError = true;
            e.printStackTrace();
        } catch (ServletException e) {
            hadError = true;
            e.printStackTrace();
        }

        assertEquals(false, hadError);
        assertEquals(403, rep.getStatus());
    }

    @Test
    public void test5_csrfFilterNoCookie() {
        boolean hadError = false;
        CsrfFilter csrfFilter = new CsrfFilter();
        req.addHeader("X-CSRF-TOKEN", "sf927dfsg84665fdg45df");
        try {
            csrfFilter.doFilter(req, rep, chain);
        } catch (IOException e) {
            hadError = true;
            e.printStackTrace();
        } catch (ServletException e) {
            hadError = true;
            e.printStackTrace();
        }

        assertEquals(false, hadError);
        assertEquals(403, rep.getStatus());
    }
}
