package fr.sii.config.filter;

import fr.sii.service.admin.config.ApplicationConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tmaugin on 22/08/2015.
 */
public class SubmissionFilter implements Filter {

    private final String SUBMISSION_DISABLED = "Submissions disabled";

    @Autowired
    private ApplicationConfigService applicationConfigService;

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

        boolean submissionsAllowed = applicationConfigService.isCfpOpen();

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