package fr.sii.config.spreadsheet;

/**
 * Created by tmaugin on 02/04/2015.
 */
import fr.sii.repository.spreadsheet.ProductionSpreadsheetRepository;
import fr.sii.repository.spreadsheet.SpreadsheetRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpreadsheetConfig {
    @Bean
    public SpreadsheetRepository googleRepository() {
        return new ProductionSpreadsheetRepository();
    }
}