package com.kwewk.course_project.config.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DataSourceConfig {

    private final Environment environment;

    @Autowired
    public DataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Primary
    public DataSourceRouting dataSourceRouting() {
        DataSourceRouting routingDataSource = new DataSourceRouting();

        routingDataSource.setEnvironment(environment);

        DataSource defaultDataSource = DataSourceBuilder.create()
                .url(environment.getProperty("spring.datasource.url"))
                .username(environment.getProperty("basic.datasource.username"))
                .password(environment.getProperty("basic.datasource.password"))
                .driverClassName(environment.getProperty("spring.datasource.driver-class-name"))
                .build();

        Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();
        targetDataSources.put("default", defaultDataSource);
        targetDataSources.put("guest", defaultDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);

        return routingDataSource;
    }
}