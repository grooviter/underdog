package com.github.grooviter.underdog.db

record PaginationResult<T>(List<T> data = [], Integer totalCount = 0) {}
