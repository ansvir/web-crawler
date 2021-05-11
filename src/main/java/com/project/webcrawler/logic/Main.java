package com.project.webcrawler.logic;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer[]> statistics = new WebCrawler().crawl("http://www.example.com/", "domain", "permission");
//        new WebCrawler().crawl("https://en.wikipedia.org/wiki/Web_crawler", "crawl", "web", "spider");
        CSVProcessor csvProcessor = new CSVProcessor();
        csvProcessor.createAllStatReport(statistics);
        csvProcessor.createTopTenStatReport(statistics);
    }
}
