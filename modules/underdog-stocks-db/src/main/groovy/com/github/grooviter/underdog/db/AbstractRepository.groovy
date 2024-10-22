package com.github.grooviter.underdog.db

import com.github.grooviter.underdog.stocks.db.common.Historical
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.transform.TailRecursive
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString

import javax.sql.DataSource
import java.lang.annotation.Annotation
import java.lang.reflect.Field
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

abstract class AbstractRepository<T, ID> {
    protected final DataSource dataSource
    protected final Class<T> entityClass
    protected final Sql sql

    AbstractRepository(DataSource ds, Class<T> clazz) {
        this.dataSource = ds
        this.entityClass = clazz
        this.sql = new Sql(ds)
    }

    PaginationResult<T> list() {
        return this.list(new Pagination(0, 100))
    }

    PaginationResult<T> list(Pagination pagination) {
        List<T> data = this.sql
            .rows("SELECT * FROM $tableName".toString(), pagination.offset(), pagination.max())
            .collect(this::deserialize) as List<T>

        Integer total = this.sql
            .firstRow("SELECT count(*) as count FROM $tableName".toString())
            .get("count") as Integer
        return new PaginationResult<T>(data, total)
    }

    T findById(ID id) {
        return this.sql
            .rows("SELECT * FROM $tableName WHERE $idFieldName = ?".toString(), id)
            .collect(this::deserialize)
            .find()
    }

    protected String getIdFieldName() {
        return getAllDeclaredFields(this.entityClass)
            ?.find { it.annotations.any {it instanceof Id } }
            ?.name ?: "id"
    }

    <U> U withTransaction(@ClosureParams(value = FromString, options = ['java.sql.Connection']) Closure<U> closure) {
        return this.sql.withTransaction {
            return closure(this)
        }
    }

    T save(T instance) {
        GroovyRowResult keys = this.sql
                .executeInsert(insertSQL, [idFieldName] as String[], getPropertiesFrom(instance))
                .find()

        if (instance instanceof Historical) {
            return instance
        }

        return findById(keys.get(idFieldName) as ID)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    Object[] getPropertiesFrom(T instance) {
        return instance.properties
            .findAll { k, v -> k != 'class' }
            .sort {it.key }
            .values()
            .toList() as Object[]
    }

    private String getInsertSQL() {
        List<String> instanceFieldNames = getAllDeclaredFields(this.entityClass)
            .name
            .sort()

        String placeholders = instanceFieldNames.collect { "?" }.join(",")
        String fieldNames = instanceFieldNames.collect { "$it" }.join(",")
        return "INSERT INTO $tableName ($fieldNames) VALUES ($placeholders)"
    }

    protected String getTableName() {
        Annotation annotation = this.entityClass.annotations.find { it instanceof Table }

        if (annotation && annotation instanceof Table) {
            return annotation.value()
        }

        return this.entityClass.simpleName.uncapitalize()
    }

    @TailRecursive
    private List<Field> getAllDeclaredFields(Class clazz, List<Field> agg = []) {
        List<Field> currentLevelFields = clazz
            .declaredFields
            .findAll {
                !(
                    it.name.startsWith('$') ||
                    it.name.startsWith('_') ||
                    it.name == 'metaClass'
                )
            } + agg

        if (!clazz.superclass) {
            return currentLevelFields
        }

        return getAllDeclaredFields(clazz.superclass, currentLevelFields)
    }

    T deserialize(GroovyRowResult row) {
        T newInstance = this.entityClass.getDeclaredConstructor().newInstance()

        this.getAllDeclaredFields(this.entityClass).each {
            newInstance[it.name] = cast(row.get(it.name), it.type)
        }

        return newInstance
    }

    static <U> U cast(Object o, Class<U> to) {
        Map<Class, Map<Class, Closure>> mappings = [
            (java.sql.Date): [
                (LocalDate): { java.sql.Date date -> date.toLocalDate() },
                (LocalDateTime): { java.sql.Date date -> date.toLocalDateTime() }
            ],
            (Timestamp): [
                (LocalDate): { Timestamp date -> date.toLocalDate() },
                (LocalDateTime): { Timestamp date -> date.toLocalDateTime() }
            ]
        ]

        return mappings[o.class]?.get(to)?.call(o) as U ?: o as U
    }
}
