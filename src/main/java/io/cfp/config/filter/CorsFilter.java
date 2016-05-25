/*
 * Copyright (c) 2016 BreizhCamp
 * [http://breizhcamp.org]
 *
 * This file is part of CFP.io.
 *
 * CFP.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package io.cfp.config.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class CorsFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (CorsUtils.isCorsRequest(request) && CorsUtils.isPreFlightRequest(request)) {
            String referer = request.getHeader(HttpHeaders.REFERER);
            if (referer.endsWith(".cfp.io") || referer.startsWith("localhost:")) {
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, referer);
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            }
        }

        filterChain.doFilter(request, response);
    }
}
