package fr.sii.config.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter used to put ip into log MDC
 */
@WebFilter
public class IpMDCFilter implements Filter {
    public static final String REQ_CLIENT = "req.client";


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        MDC.put(REQ_CLIENT, request.getRemoteHost());

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            String forwarded = httpServletRequest.getHeader("X-Forwarded-For");
            if (forwarded != null) {
                MDC.put(REQ_CLIENT, forwarded);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQ_CLIENT);
        }
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
