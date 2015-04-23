package fr.sii.config.email;

/**
 * Created by tmaugin on 02/04/2015.
 */
import fr.sii.repository.email.EmailingRepository;
import fr.sii.repository.email.ProductionEmailingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailingConfig {
    @Bean
    public EmailingRepository emailingRepository() {
        return new ProductionEmailingRepository();
    }
}