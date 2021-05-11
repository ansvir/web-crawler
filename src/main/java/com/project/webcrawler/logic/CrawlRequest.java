package com.project.webcrawler.logic;

import lombok.Data;

import java.util.List;

@Data
public class CrawlRequest {
    private String seed;
    private List<String> terms;
}
