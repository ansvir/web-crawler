package com.project.webcrawler.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private List<Link> links;

    public Query() {}

    public Query(List<Link> links) {
        this.links = links;
    }
}
