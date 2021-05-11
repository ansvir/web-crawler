package com.project.webcrawler.repository;

import com.project.webcrawler.domain.Query;
import org.springframework.data.repository.CrudRepository;

public interface QueryRepository extends CrudRepository<Query, Long> {
    public Query findDistinctTop10ById(Long id);
}
