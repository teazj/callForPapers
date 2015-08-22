package fr.sii.config.filter;

import com.google.appengine.api.datastore.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tmaugin on 22/08/2015.
 */
public class SubmissionFilter implements Filter {

    private final String SUBMISSION_DISABLED = "Submissions disabled";

    /**
     * Reject request if submissions not allowed
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Key applicationConfigKey = KeyFactory.createKey("Config", "Application");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        boolean submissionsAllowed = false;
        try {
            Entity refreshToken = datastore.get(applicationConfigKey);
            submissionsAllowed = (boolean) refreshToken.getProperty("enableSubmissions");
        } catch (EntityNotFoundException e) {
            // submissionsAllowed = false;
        }

        if(!submissionsAllowed) {
            switch(httpRequest.getMethod().toUpperCase()) {
                case "POST" :
                case "PUT" :
                    httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, SUBMISSION_DISABLED);
                    return;
                default :
                    break;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { /* unused */ }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { /* unused */ }

}