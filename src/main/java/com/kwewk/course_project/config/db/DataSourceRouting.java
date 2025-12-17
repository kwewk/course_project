package com.kwewk.course_project.config.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
public final class DataSourceRouting extends AbstractRoutingDataSource {

    private Map<Object, Object> targetDataSources;
    private Environment environment;

    @Override
    protected Object determineCurrentLookupKey() {
        String key = DataSourceContextHolder.get();
        log.debug("Determining DataSource for key: {}", key);
        return key != null ? key : "default";
    }

    @Override
    public void setTargetDataSources(@NonNull Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        this.targetDataSources = targetDataSources;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void addDataSource(String username, String password) {
        if (dataSourceExists(username)) {
            log.debug("DataSource for user {} already exists", username);
            return;
        }

        String url = environment.getProperty("spring.datasource.url");
        String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

        DataSource dataSource = DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();

        this.targetDataSources.put(username, dataSource);
        super.afterPropertiesSet();

        log.info("Created new DataSource for user: {}", username);
    }

    public void removeDataSource(String key) {
        if (!dataSourceExists(key)) {
            log.warn("DataSource for key {} does not exist", key);
            return;
        }

        Object ds = this.targetDataSources.get(key);
        if (ds instanceof HikariDataSource) {
            ((HikariDataSource) ds).close();
        }

        this.targetDataSources.remove(key);
        super.afterPropertiesSet();

        log.info("Removed DataSource for user: {}", key);
    }

    public boolean dataSourceExists(String key) {
        return this.targetDataSources.containsKey(key);
    }
}