package fr.sii.config.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tmaugin on 22/05/2015.
 */
public class CsrfFilter implements Filter {

    private static final String CSRF_TOKEN = "CSRF-TOKEN";
    private static final String X_CSRF_TOKEN = "X-CSRF-TOKEN";
    private static final String BAD_TOKEN = "Cookie CSRF token %s doesn't match header CSRF token %s";
    private static final String NO_TOKEN = "No CSRF token was found on the request parameter header %s and/or cookie %s";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Do filter for CSRF
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        final String csrfTokenValue = httpRequest.getHeader(X_CSRF_TOKEN);
        final Cookie[] cookies = httpRequest.getCookies();

        // No CSRF filter for dev administration page
        String path = httpRequest.getRequestURI();
        if (path.startsWith("/_ah/")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (DefaultRequiresCsrfMatcher.matches(httpRequest)) {
            String csrfCookieValue = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(CSRF_TOKEN)) {
                        csrfCookieValue = cookie.getValue();
                    }
                }
            }

            if (csrfTokenValue == null || csrfCookieValue == null) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.getWriter().write(String.format(NO_TOKEN, X_CSRF_TOKEN, CSRF_TOKEN));
                httpResponse.getWriter().flush();
                httpResponse.getWriter().close();
                return;
            }

            if (!csrfTokenValue.equals(csrfCookieValue)) {
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.getWriter().write(String.format(BAD_TOKEN, csrfCookieValue, csrfTokenValue));
                httpResponse.getWriter().flush();
                httpResponse.getWriter().close();
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    public static final class DefaultRequiresCsrfMatcher{
        private static final ArrayList<String> allowedMethods = new ArrayList<String>() {{
            add("GET");
            add("OPTIONS");
            add("TRACE");
            add("HEAD");
        }};

        public static boolean matches(HttpServletRequest request) {
            return !allowedMethods.contains(request.getMethod());
        }
    }
}
