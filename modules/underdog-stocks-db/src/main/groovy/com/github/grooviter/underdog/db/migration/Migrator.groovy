package com.github.grooviter.underdog.db.migration

import groovy.transform.TupleConstructor

import javax.sql.DataSource

@TupleConstructor
class Migrator {
    DataSource dataSource

    void execute() {
        MigrationRepository migrationRepository = new MigrationRepository(dataSource)
        migrationRepository.createTableIfNotExists()

        List<Migration> applied = migrationRepository.list().data()
        List<Migration> missing = parseMigrationFiles() - applied

        missing.each {migration ->
            if (migrationRepository.hasSignatureChanged(migration)) {
                throw new RuntimeException("signature of migration ${migration.name} has changed!")
            }

            migrationRepository.withTransaction {
                migrationRepository.applyMigration(migration)
                migrationRepository.save(migration)
            }
        }
    }

    private List<Migration> parseMigrationFiles() {
        URL url = this.class.classLoader.getResource("underdog/migrations")
        return new File(url.file)
            .listFiles()
            .collect {
                new Migration(it.name, it.text, it.text.sha256())
            }
    }
}
