package com.github.grooviter.underdog.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

class BaseSpec extends Specification {
    @Shared
    PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres:17.0")
            .withDatabaseName("foo")
            .withUsername("foo")
            .withPassword("secret")

    DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig()
        hikariConfig.setJdbcUrl(dbContainer.jdbcUrl)
        hikariConfig.setUsername(dbContainer.username)
        hikariConfig.setPassword(dbContainer.password)
        HikariDataSource ds = new HikariDataSource(hikariConfig)
        return ds
    }
}
