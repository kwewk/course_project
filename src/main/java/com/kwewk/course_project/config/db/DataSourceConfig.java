package com.kwewk.course_project.config.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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

        String url = environment.getProperty("spring.datasource.url");
        String driverClassName = environment.getProperty("spring.datasource.driver-class-name");

        DataSource guestDataSource = DataSourceBuilder.create()
                .url(url)
                .username("guest_role")
                .password("guest_password_change_me")
                .driverClassName(driverClassName)
                .build();

        DataSource mainUserDataSource = DataSourceBuilder.create()
                .url(url)
                .username("main_user")
                .password("user_password_change_me")
                .driverClassName(driverClassName)
                .build();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("guest", guestDataSource);
        targetDataSources.put("main_user", mainUserDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(mainUserDataSource);

        return routingDataSource;
    }
}