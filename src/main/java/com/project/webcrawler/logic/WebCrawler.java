package com.project.webcrawler.logic;

import com.project.webcrawler.controller.CrawlingController;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.webcrawler.logic.Constant.MAX_LINK_DEPTH;
import static com.project.webcrawler.logic.Constant.MAX_PAGES_VISITED;

@Component
public class WebCrawler {
    private Set<String> links;
    private Set<String> newLinks;
    private Set<String> visitedPages;

    @Autowired
    private CrawlingController crawlingController;

    public WebCrawler() {
        links = new LinkedHashSet<>();
        newLinks = new LinkedHashSet<>();
        visitedPages = new HashSet<>();
    }

    public Map<String, Integer[]> crawl(String seed, String... terms) {
            Map<String, Integer[]> seedOccurrences = new LinkedHashMap<>();
            links.add(seed);
            for (int i = 0; i < MAX_LINK_DEPTH.getValue(); i++) {
                crawlingController.sendCrawlingLog("Current depth: " + i);
                for (String link : links) {
                    if (visitedPages.contains(link)) {
                        continue;
                    }
                    if (visitedPages.size() >= MAX_PAGES_VISITED.getValue()) {
                        crawlingController.sendCrawlingLog("Max pages visits reached. Aborting");
                        return seedOccurrences;
                    }
                    crawlingController.sendCrawlingLog("Crawling [" + link + "]");
                    try {
                        Connection.Response response = Jsoup.connect(link).execute();
                        String contentType = response.contentType();
                        if (!contentType.startsWith("text/html")) {
                            continue;
                        }
                        if (link.contains("#")) {
                            continue;
                        }
                        System.out.println(link);
                        Document document = response.parse();
                        Integer[] occurrences = findOccurrences(terms, document);
                        seedOccurrences.put(link, occurrences);
                        newLinks.addAll(getPageLinks(link));
                    } catch (UnsupportedMimeTypeException e) {
                        String message = "Unsupported mimetype for link [" + link + "]";
                        crawlingController.sendCrawlingLog(message);
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        String message = "Unsupported protocol, link [" + link + "] skipped";
                        crawlingController.sendCrawlingLog(message);
                        e.printStackTrace();
                    } catch (HttpStatusException e) {
                        String message = "HTTP status is incorrect to parse the page [" + link + "]";
                        crawlingController.sendCrawlingLog(message);
                        e.printStackTrace();
                    } catch (IOException | IllegalArgumentException e) {
                        String message = "Link [" + link + "] skipped according to some reason";
                        crawlingController.sendCrawlingLog(message);
                        e.printStackTrace();
                    }
                    finally {
                        visitedPages.add(link);
                    }
                }
                links.addAll(newLinks);
                newLinks.clear();
            }

            for (Map.Entry<String,Integer[]> entry : seedOccurrences.entrySet()) {
                System.out.print(entry.getKey() + " ");
                for (Integer hit : entry.getValue()) {
                    System.out.print(hit + " ");
                }
                System.out.println();
            }
            links.clear();
            visitedPages.clear();
        return seedOccurrences;
    }

    private Integer[] findOccurrences(String[] terms, Document document) {
        Integer[] occurrences = new Integer[terms.length];
        Arrays.fill(occurrences, 0);
        String bodyText = document.body().text();
        for (int i = 0; i < terms.length; i++) {
            Pattern pattern = Pattern.compile(terms[i]);
            Matcher matcher = pattern.matcher(bodyText);
            while (matcher.find()) {
                occurrences[i]++;
            }
        }
        return occurrences;
    }

    private Set<String> getPageLinks(String link) throws IOException {
        Set<String> links = new LinkedHashSet<>();
        Document document = Jsoup.connect(link).get();
        Elements linksOnPage = document.select("a[href]");
        for (Element linkOnPage : linksOnPage) {
            String linkString = linkOnPage.attr("abs:href");
            links.add(linkString);
        }
        return links;
    }

}