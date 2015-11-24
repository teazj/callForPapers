package fr.sii.config.filter;

import com.nimbusds.jwt.JWTClaimsSet;
import fr.sii.entity.AdminUser;
import fr.sii.service.admin.user.AdminUserService;
import fr.sii.service.auth.AuthUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter reading auth token and check if user is admin
 */
public class AuthAdminFilter extends AuthFilter {

    private AdminUserService adminUserService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        adminUserService = webApplicationContext.getBean(AdminUserService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader(AuthUtils.AUTH_HEADER_KEY);

        try {
            JWTClaimsSet token = readToken(authHeader);
            int userId = Integer.parseInt(token.getSubject());

            AdminUser admin = adminUserService.findFromUserId(userId);
            if (admin == null) throw new InvalidTokenException(HttpServletResponse.SC_UNAUTHORIZED, AUTH_ERROR_MSG);

            adminUserService.setCurrentAdmin(admin);
            chain.doFilter(request, response);

        } catch (InvalidTokenException e) {
            httpResponse.sendError(e.getResponseCode(), e.getMessage());
        }
    }
}
