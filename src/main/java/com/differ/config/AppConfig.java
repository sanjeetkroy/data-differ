package com.differ.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean(name = "db1")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource1() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplateAurora")
    public JdbcTemplate jdbcTemplateAurora(@Qualifier("db1") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean(name = "db2")
    @ConfigurationProperties(prefix = "db2.datasource")
    public DataSource dataSource2() {
        return  DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplateMysql")
    public JdbcTemplate jdbcTemplateMysql(@Qualifier("db2") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
