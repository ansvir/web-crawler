package com.project.webcrawler.repository;

import com.project.webcrawler.domain.Link;
import org.springframework.data.repository.CrudRepository;

public interface LinkRepository extends CrudRepository<Link, Long> {
}
