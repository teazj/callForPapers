package io.cfp.controller.config.security;

import io.cfp.SecurityConfiguration;
import io.cfp.repository.RoleRepository;
import io.cfp.repository.UserRepo;
import io.cfp.service.user.SecurityUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Properties;

import static org.mockito.Mockito.mock;

@Configuration
@EnableWebMvc
@Import(SecurityConfiguration.class)
public class SecurityConfig {

    @Bean
    public SecurityUserService userDetailsService() {
        return new SecurityUserService();
    }

    @Bean
    public UserRepo userRepo() {
        return mock(UserRepo.class);
    }

    @Bean
    public RoleRepository roleRepository() {
        return mock(RoleRepository.class);
    }
}
