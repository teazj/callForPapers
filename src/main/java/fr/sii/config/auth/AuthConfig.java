package fr.sii.config.auth;

import fr.sii.entity.AdminUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * Configure authentication beans
 */
@Configuration
public class AuthConfig {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AdminUser connectedAdmin() {
        return new AdminUser();
    }

}
