package fr.sii.email;

/**
 * Created by tmaugin on 02/04/2015.
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailingConfig {
    @Bean
    public EmailingRepository emailingRepository() {
        return new ProductionEmailingRepository();
    }
}