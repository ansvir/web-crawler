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
    private String name;
    @OneToMany
    private List<Link> links;
}
