package fr.sii.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.zaxxer.hikari.HikariDataSource;

import liquibase.integration.spring.SpringLiquibase;

/**
 * Settings for MySQL database connection
 */
@Configuration
@EnableJpaRepositories("fr.sii.repository")
public class DatabaseConfig {

    @Value("${db.host}")
    private String host;

    @Value("${db.name}")
    private String name;

    @Value("${db.user:''}")
    private String user;

    @Value("${db.pass:''}")
    private String pass;

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();

        ds.setJdbcUrl("jdbc:mysql://" + host + (host.endsWith("/") ? "" : "/") + name);
        if (user != null && user.length() > 0) {
            ds.setUsername(user);
        }
        if (pass != null && pass.length() > 0) {
            ds.setPassword(pass);
        }

        ds.addDataSourceProperty("prepStmtCacheSize", 250);
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        ds.addDataSourceProperty("cachePrepStmts", true);

        return ds;
    }

    /**
     * Upgrade database structure on starting Spring container
     *
     * @return liquibase bean updating database
     */
    @Bean
    public SpringLiquibase springLiquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:changelog/changelog-master.xml");
        return liquibase;
    }
}
