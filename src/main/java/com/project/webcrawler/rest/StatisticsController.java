package com.project.webcrawler.rest;

import com.project.webcrawler.domain.Hit;
import com.project.webcrawler.domain.Link;
import com.project.webcrawler.domain.Query;
import com.project.webcrawler.logic.CSVProcessor;
import com.project.webcrawler.logic.CrawlRequest;
import com.project.webcrawler.logic.WebCrawler;
import com.project.webcrawler.repository.HitRepository;
import com.project.webcrawler.repository.LinkRepository;
import com.project.webcrawler.repository.QueryRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        for (int i = 0; i < queryLinks.size(); i++) {
            for(Link link : queryLinks) {
                Integer[] hitsIntegerArray = new Integer[link.getHits().size()];
                List<Hit> hits = link.getHits();
                for (int j = 0; j < hitsIntegerArray.length; j++) {
                    hitsIntegerArray[i] = hits.get(i).getHit();
                }
                statistics.put(link.getName(), hitsIntegerArray);
            }
        }
        CSVProcessor csvProcessor = new CSVProcessor();
        File csvFile = csvProcessor.createAllStatReport(statistics);
        InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(csvFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return ResponseEntity.ok()
                .contentLength(csvFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/add")
    public void addCrawling(@RequestBody CrawlRequest crawlRequest) {
        WebCrawler webCrawler = new WebCrawler();
        Map<String, Integer[]> statistics = webCrawler.crawl(crawlRequest.getSeed(), (String[]) crawlRequest.getTerms().toArray());
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
        queryRepository.save(query);
    }

    @GetMapping("/top/ten/{id}")
    public ResponseEntity<Resource> getTopTenStatistics(@PathParam("id") Long id) {
        Optional<Query> optQuery = queryRepository.findById(id);
        Query query;
        if (optQuery.isPresent()) {
            query = optQuery.get();
        } else {
            return null;
        }
        List<Link> queryLinks = query.getLinks();
        Map<String, Integer[]> statistics = new HashMap<>(queryLinks.size());
        for (int i = 0; i < queryLinks.size(); i++) {
            for(Link link : queryLinks) {
                Integer[] hitsIntegerArray = new Integer[link.getHits().size()];
                List<Hit> hits = link.getHits();
                for (int j = 0; j < hitsIntegerArray.length; j++) {
                    hitsIntegerArray[i] = hits.get(i).getHit();
                }
                statistics.put(link.getName(), hitsIntegerArray);
            }
        }
        CSVProcessor csvProcessor = new CSVProcessor();
        File csvFile = csvProcessor.createAllStatReport(statistics);
        InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(csvFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return ResponseEntity.ok()
                .contentLength(csvFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
