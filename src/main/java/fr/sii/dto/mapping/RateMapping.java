package fr.sii.dto.mapping;

import fr.sii.config.mapping.Mapping;
import fr.sii.dto.RateAdmin;
import fr.sii.entity.Rate;
import ma.glasnost.orika.MapperFactory;
import org.springframework.stereotype.Component;

@Component
public class RateMapping implements Mapping {

    @Override
    public void mapClasses(MapperFactory mapperFactory) {
        mapperFactory.classMap(Rate.class, RateAdmin.class)
            .fieldAToB("adminUser", "user")
            .byDefault()
            .register();
    }
}
