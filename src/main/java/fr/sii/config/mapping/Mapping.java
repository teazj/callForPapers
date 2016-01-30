package fr.sii.config.mapping;

import ma.glasnost.orika.MapperFactory;

/**
 * Interface to implement in order to define DTO mapping
 */
public interface Mapping {
    /**
     * Method called to define DTO mapping
     *
     * @param mapperFactory Orika Factory
     */
    void mapClasses(MapperFactory mapperFactory);
}
