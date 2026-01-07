package com.kwewk.course_project.config.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.NonNull;

@Slf4j
public final class DataSourceRouting extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String key = DataSourceContextHolder.get();
        log.debug("Determining DataSource for key: {}", key);
        if ("guest".equals(key)) {
            return "guest";
        }
        return "main_user";
    }

    @Override
    public void setTargetDataSources(@NonNull java.util.Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
    }
}