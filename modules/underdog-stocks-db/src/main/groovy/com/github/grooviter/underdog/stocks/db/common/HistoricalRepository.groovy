package com.github.grooviter.underdog.stocks.db.common

import com.github.grooviter.underdog.db.AbstractRepository
import com.github.grooviter.underdog.db.Pagination
import com.github.grooviter.underdog.db.PaginationResult
import groovy.transform.InheritConstructors

@InheritConstructors
abstract class HistoricalRepository<T extends Historical, ID> extends AbstractRepository<T, ID> {

    PaginationResult<T> findAllBetweenDates(
            Date from,
            Date to, Pagination pagination = new Pagination(0, 100)) {
        List<T> data = this.sql
            .rows(
                "SELECT * FROM ${tableName} WHERE date >= :from AND date <= :to",
                [from, to],
                pagination.offset(),
                pagination.max())
            .collect(this::deserialize) as List<T>
        return new PaginationResult<T>(data, 0)
    }
}
