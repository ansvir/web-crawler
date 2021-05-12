package com.project.webcrawler.controller;

import com.project.webcrawler.domain.Hit;
import com.project.webcrawler.domain.Link;

import com.project.webcrawler.domain.Query;
import com.project.webcrawler.logic.CSVProcessor;
import com.project.webcrawler.logic.CrawlRequest;
import com.project.webcrawler.logic.WebCrawler;
import com.project.webcrawler.repository.HitRepository;
import com.project.webcrawler.repository.LinkRepository;
import com.project.webcrawler.repository.QueryRepository;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/stat")
public class StatisticsController {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private HitRepository hitRepository;

    @Autowired
    private WebCrawler webCrawler;

    @GetMapping("/all/{id}")
    public ResponseEntity<Resource> getAllStatistics(@PathVariable Long id) {
        Optional<Query> optQuery = queryRepository.findById(id);
        Query query;
        if (optQuery.isPresent()) {
            query = optQuery.get();
        } else {
            return null;
        }
        List<Link> queryLinks = query.getLinks();
        Map<String, Integer[]> statistics = new HashMap<>(queryLinks.size());
        for (Link link : queryLinks) {
            Integer[] hitsIntegerArray = new Integer[link.getHits().size() + 1];
            List<Hit> hits = link.getHits();
            for (int j = 0; j < hits.size(); j++) {
                hitsIntegerArray[j] = hits.get(j).getHit();
            }
            hitsIntegerArray[hitsIntegerArray.length - 1] = hitRepository.sumHitsByLinkId(link.getId());
            statistics.put(link.getName(), hitsIntegerArray);
        }
        CSVProcessor csvProcessor = new CSVProcessor();
        File csvFile = csvProcessor.createAllStatReport(statistics);
        InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(csvFile));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.csv");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(csvFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/add")
    public void addCrawling(@RequestBody CrawlRequest crawlRequest) {
        List<String> termsList = crawlRequest.getTerms();
        String[] termsArray = new String[termsList.size()];
        for (int i = 0; i < termsList.size(); i++) {
            termsArray[i] = termsList.get(i);
        }
        Map<String, Integer[]> statistics = webCrawler.crawl(crawlRequest.getSeed(), termsArray);
        Query query = new Query();
        List<Link> links = new ArrayList<>();
        for (Map.Entry<String, Integer[]> stat : statistics.entrySet()) {
            Link link = new Link();
            link.setName(stat.getKey());
            List<Hit> hits = new ArrayList<>();
            for (Integer stat1 : stat.getValue()) {
                Hit hit = new Hit();
                hit.setHit(stat1);
                Hit savedHit = hitRepository.save(hit);
                hits.add(savedHit);
            }
            link.setHits(hits);
            Link savedLink = linkRepository.save(link);
            links.add(savedLink);
        }
        query.setLinks(links);
        Query savedQuery = queryRepository.save(query);
        DateTimeFormatter dateFormat = DateTimeFormat
                .forPattern("d-M-Y H:m:s");
        savedQuery.setName("Query" + savedQuery.getId() + " " + dateFormat.print(new LocalDateTime()));
        queryRepository.save(query);
    }

    @GetMapping("/top/ten/{id}")
    public ResponseEntity<Resource> getTopTenStatistics(@PathVariable Long id) {
        Optional<Query> optQuery = queryRepository.findById(id);
        Query query;
        if (optQuery.isPresent()) {
            query = optQuery.get();
        } else {
            return null;
        }
        List<Link> queryLinks = query.getLinks();
        Map<String, Integer[]> statistics = new HashMap<>(queryLinks.size());
        for (Link link : queryLinks) {
            Integer[] hitsIntegerArray = new Integer[link.getHits().size() + 1];
            List<Hit> hits = link.getHits();
            for (int j = 0; j < hits.size(); j++) {
                hitsIntegerArray[j] = hits.get(j).getHit();
            }
            hitsIntegerArray[hitsIntegerArray.length - 1] = hitRepository.sumHitsByLinkId(link.getId());
            statistics.put(link.getName(), hitsIntegerArray);
        }
        CSVProcessor csvProcessor = new CSVProcessor();
        File csvFile = csvProcessor.createTopTenStatReport(statistics);
        InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(csvFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=topTenReport.csv");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(csvFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/query/all")
    public List<Query> getALlQueries() {
        return (List<Query>) queryRepository.findAll();
    }
}
