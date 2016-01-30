package fr.sii.config.mapping;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for Orika mapper
 */
@Configuration
public class MappingConfig {

    @Autowired(required = false)
    private List<CustomConverter<?, ?>> converters;

    @Autowired(required = false)
    private List<Mapping> mappings;

    @Bean
    public MapperFactory mapperFactory() {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        //register here custom converter and mapper
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        if (converters != null && !converters.isEmpty()) {
            for (CustomConverter<?, ?> converter : converters) {
                converterFactory.registerConverter(converter);
            }
        }

        if (mappings != null && !mappings.isEmpty()) {
            for (Mapping mapping : mappings) {
                mapping.mapClasses(mapperFactory);
            }
        }

        return mapperFactory;
    }

    @Bean
    public MapperFacade mapperFacade(MapperFactory mapperFactory) {
        return mapperFactory.getMapperFacade();
    }
}
