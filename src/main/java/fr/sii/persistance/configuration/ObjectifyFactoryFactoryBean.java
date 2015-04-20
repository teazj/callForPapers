package fr.sii.persistance.configuration;

/**
 * Created by tmaugin on 20/04/2015.
 */
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.impl.translate.opt.BigDecimalLongTranslatorFactory;

public class ObjectifyFactoryFactoryBean implements FactoryBean<ObjectifyFactory>, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(ObjectifyFactoryFactoryBean.class);

    private ObjectifyFactory objectifyFactory;

    private String basePackage;

    @Override
    public ObjectifyFactory getObject() throws Exception {
        return objectifyFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return ObjectifyFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws ClassNotFoundException {
        /*ObjectifyFactory objectifyFactory = ObjectifyService.factory();

        JodaTimeTranslators.add(objectifyFactory);
        objectifyFactory.getTranslators().add(new BigDecimalLongTranslatorFactory());

        log.info("Finding classes with @Entity or @EntitySubclass annotations in {} to register on ObjectifyFactory", basePackage);
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
            log.info("Registering class {}", bd.getBeanClassName());
            objectifyFactory.register(Class.forName(bd.getBeanClassName()));
        }

        this.objectifyFactory = objectifyFactory;*/
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}