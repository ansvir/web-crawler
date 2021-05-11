package com.project.webcrawler.repository;

import com.project.webcrawler.domain.Hit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface HitRepository extends CrudRepository<Hit, Long> {
    @Query(
            value = "SELECT SUM(hit) FROM hit h " +
                    "JOIN link_hits lh ON lh.hits_id = h.id " +
                    "JOIN link l ON lh.link_id = l.id " +
                    "WHERE l.id = ?1 " +
                    "GROUP BY l.id",
            nativeQuery = true)
    Integer sumHitsByLinkId(Long id);
}
