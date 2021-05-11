package com.project.webcrawler.repository;

import com.project.webcrawler.domain.Hit;
import org.springframework.data.repository.CrudRepository;

public interface HitRepository extends CrudRepository<Hit, Long> {
}
