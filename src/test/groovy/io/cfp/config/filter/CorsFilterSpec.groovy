package io.cfp.config.filter

import org.springframework.http.HttpHeaders
import spock.lang.Specification

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by jl on 11/06/16.
 */
class CorsFilterSpec extends Specification {

    CorsFilter filter = new CorsFilter()

    def 'port number in Origin header should not be considered'() {

        given:
        def request = Mock(HttpServletRequest) {
            getHeader(HttpHeaders.ORIGIN) >> origin
        }
        def response = Mock(HttpServletResponse)
        def filterChain = Mock(FilterChain)

        when:
        filter.doFilterInternal(request, response, filterChain)

        then:
        1 * response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin)
        1 * filterChain.doFilter(request, response)

        where:
        origin << ['http://localhost', 'http://localhost:3000', 'https://foo.cfp.io:3000', 'http://foo.cfp.io']

    }

    // TODO implement other tests
}
