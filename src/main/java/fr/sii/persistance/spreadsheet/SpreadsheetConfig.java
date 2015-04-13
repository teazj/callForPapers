package fr.sii.persistance.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpreadsheetConfig {
    @Bean
    public SpreadsheetRepository googleRepository() {
        return new ProductionSpreadsheetRepository();
    }
}