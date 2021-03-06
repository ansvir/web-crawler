package com.project.webcrawler.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1000)
    private String name;
    @OneToMany
    private List<Hit> hits;

    public Link() {}

    public Link(String name) {
        this.name = name;
    }
}
