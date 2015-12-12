package fr.sii.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nicolas on 12/12/2015.
 */
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        Cache<Object, Object> applicationSettings = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build();

        Cache<Object, Object> isCfpOpen = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build();

        cacheManager.setCaches(Arrays.asList(new GuavaCache("applicationSettings", applicationSettings), new GuavaCache("isCfpOpen", isCfpOpen)));

        return cacheManager;
    }
}
