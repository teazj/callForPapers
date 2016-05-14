package io.cfp.controller.config.security;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.cfp.SecurityConfiguration;
import io.cfp.repository.RoleRepository;
import io.cfp.repository.UserRepo;

@Configuration
@EnableWebMvc
@Import(SecurityConfiguration.class)
public class SecurityConfig {

    @Bean
    public UserRepo userRepo() {
        return mock(UserRepo.class);
    }

    @Bean
    public RoleRepository roleRepository() {
        return mock(RoleRepository.class);
    }
}
