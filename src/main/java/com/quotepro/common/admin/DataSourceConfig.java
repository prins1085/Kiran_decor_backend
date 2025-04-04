package com.quotepro.common.admin;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    private final Environment env;

    public DataSourceConfig(Environment env) {
        this.env = env;
    }
    
    @Primary
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword("implies$#*@");
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        dataSource.setMaximumPoolSize(env.getProperty("spring.datasource.hikari.maximum-pool-size", Integer.class, 1000));
        dataSource.setMinimumIdle(env.getProperty("spring.datasource.hikari.minimum-idle", Integer.class, 1));
        dataSource.setIdleTimeout(env.getProperty("spring.datasource.hikari.idle-timeout", Long.class, 10000L));
        dataSource.setMaxLifetime(env.getProperty("spring.datasource.hikari.max-lifetime", Long.class, 60000L));
        dataSource.setConnectionTimeout(env.getProperty("spring.datasource.hikari.connection-timeout", Long.class, 30000L));
        dataSource.setConnectionInitSql(env.getProperty("spring.datasource.hikari.connection-init-sql","SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED"));
        return dataSource;
    }
}