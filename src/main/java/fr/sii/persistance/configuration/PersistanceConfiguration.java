package fr.sii.persistance.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tmaugin on 20/04/2015.
 */

@Configuration
public class PersistanceConfiguration {
    @Bean
    public ObjectifyFactoryFactoryBean objectifyFactoryFactoryBean()
    {
        ObjectifyFactoryFactoryBean objectifyFactoryFactoryBean = new ObjectifyFactoryFactoryBean();
        objectifyFactoryFactoryBean.setBasePackage("fr.sii");
        return objectifyFactoryFactoryBean;
    }
}
