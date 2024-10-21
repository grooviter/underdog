package com.github.grooviter.underdog.db.migration

import com.github.grooviter.underdog.db.AbstractRepository
import groovy.util.logging.Slf4j

import javax.sql.DataSource

@Slf4j
class MigrationRepository extends AbstractRepository<Migration, String> {
    MigrationRepository(DataSource ds) {
        super(ds, Migration)
    }

    void applyMigration(Migration migration) {
        this.sql.execute(migration.sql)
    }

    void createTableIfNotExists() {
        try {
            this.sql.execute("CREATE TABLE $tableName (name varchar(100) PRIMARY KEY, sql TEXT, sha256 varchar(256));".toString())
        } catch (Exception ex) {
            log.warn("table already created: ${ex.message}")
        }
    }
}
