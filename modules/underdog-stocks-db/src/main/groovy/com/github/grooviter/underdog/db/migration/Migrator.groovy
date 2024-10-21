package com.github.grooviter.underdog.db.migration

import com.github.grooviter.underdog.db.Pagination
import groovy.transform.TupleConstructor

import javax.sql.DataSource

@TupleConstructor
class Migrator {
    DataSource dataSource

    void execute() {
        // 0. repository
        MigrationRepository migrationRepository = new MigrationRepository(dataSource)

        // 1. createTableIfNotExists()
        migrationRepository.createTableIfNotExists()

        // 2. parseMigrationFiles()
        List<Migration> applied = migrationRepository.list(new Pagination(0, 1000)).data()
        List<Migration> missing = parseMigrationFiles() - applied

        // 3. checkIntegrity()
        // TODO

        // 4. apply(Migration)
        missing.each {migration ->
            migrationRepository.withTransaction {
                migrationRepository.applyMigration(migration)
                migrationRepository.save(migration)
            }
        }
    }

    List<Migration> parseMigrationFiles() {
        URL url = this.class.classLoader.getResource("underdog/migrations")
        return new File(url.file)
            .listFiles()
            .collect {
                new Migration(it.name, it.text, it.text.sha256())
            }
    }
}
