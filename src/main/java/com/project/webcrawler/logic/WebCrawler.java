package com.project.webcrawler.logic;

import com.project.webcrawler.controller.CrawlingController;
import com.project.webcrawler.websockets.StopCrawlingException;
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
import java.net.SocketTimeoutException;
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

    /**
     * This method is used for searching occurrences of terms in seed and sub links
     * @param seed Link to start crawling from
     * @param terms Terms to search for sub links of seed
     * @return Map that contains statistics of searching for terms in seed and sub links
     */
    public Map<String, Integer[]> crawl(String seed, String... terms) {
        Map<String, Integer[]> seedOccurrences = new LinkedHashMap<>();
        boolean stopCrawling = false;
//        Map<String, Integer[]> tempSeedOccurrences = new LinkedHashMap<>();
        links.add(seed);
        for (int i = 0; i < MAX_LINK_DEPTH.getValue() && !stopCrawling; i++) {
            crawlingController.sendCurrentDepth(i);
//            try {
//                try {
//                    crawlingController.sendCurrentDepth(i);
//                    links.parallelStream().forEach(link -> {
//                        if (crawlingController.getStopRequest()) {
//                            throw new StopCrawlingException();
//                        }
//                        if (visitedPages.contains(link)) {
//                            return;
//                        }
//                        if (visitedPages.size() >= MAX_PAGES_VISITED.getValue()) {
//                            crawlingController.sendCrawlingLog("Max pages visits reached. Aborting");
//                            throw new StopCrawlingException();
//                        }
//                        crawlingController.sendPagesCrawled(visitedPages.size());
//                        System.out.println(visitedPages.size());
//                        try {
//                            crawlingController.sendCrawlingLog("Crawling [" + link + "]");
//                            Connection.Response response = Jsoup.connect(link)
//                                    .timeout(2 * 1000)
//                                    .ignoreHttpErrors(true)
//                                    .execute();
//                            if (response.statusCode() != 200) {
//                                crawlingController.sendCrawlingLog("Link responded with invalid status code. Skipping");
//                                return;
//                            }
//                            String contentType = response.contentType();
//                            if (!contentType.startsWith("text/html")) {
//                                crawlingController.sendCrawlingLog("Page format not HTML. Skipping");
//                                return;
//
//                            }
//                            if (link.contains("#")) {
//                                crawlingController.sendCrawlingLog("Link references to itself. Skipping");
//                                return;
//                            }
//                            System.out.println(link);
//                            Document document = response.parse();
//                            if (document.body() == null) {
//                                crawlingController.sendCrawlingLog("Link has no body. Skipping");
//                                return;
//                            }
//                            Integer[] occurrences = findOccurrences(terms, document);
//                            tempSeedOccurrences.put(link, occurrences);
//                            newLinks.addAll(getPageLinks(document));
//                        } catch (SocketTimeoutException e) {
//                            String message = "Timout of connecting to the page [" + link + "]";
//                            crawlingController.sendCrawlingLog(message);
//                            e.printStackTrace();
//                        } catch (UnsupportedMimeTypeException e) {
//                            String message = "Unsupported mimetype for link [" + link + "]";
//                            crawlingController.sendCrawlingLog(message);
//                            e.printStackTrace();
//                        } catch (MalformedURLException e) {
//                            String message = "Unsupported protocol, link [" + link + "] skipped";
//                            crawlingController.sendCrawlingLog(message);
//                            e.printStackTrace();
//                        } catch (HttpStatusException e) {
//                            String message = "HTTP status is incorrect to parse the page [" + link + "]";
//                            crawlingController.sendCrawlingLog(message);
//                            e.printStackTrace();
//                        } catch (IOException | IllegalArgumentException e) {
//                            String message = "Link [" + link + "] skipped according to some reason";
//                            crawlingController.sendCrawlingLog(message);
//                            e.printStackTrace();
//                        } finally {
//                            visitedPages.add(link);
//                        }
//                    });
//                } catch (StopCrawlingException e) {
//                    System.out.println("EXCEPTION 1");
//                    crawlingController.setStopRequest(false);
//                    String message = "Crawling stopped";
//                    crawlingController.sendCrawlingLog(message);
//                    break;
//                }
//            } catch (ConcurrentModificationException e) {
//                System.out.println("EXCEPTION 2");
//                crawlingController.setStopRequest(false);
//                String message = "Crawling stopped";
//                crawlingController.sendCrawlingLog(message);
//                break;
//            }
            for (String link : links) {
                if (visitedPages.contains(link)) {
                    continue;
                }
                if (visitedPages.size() >= MAX_PAGES_VISITED.getValue()) {
                    crawlingController.sendCrawlingLog("Max pages visits reached. Aborting");
                    stopCrawling = true;
                    break;
                }
                System.out.println(visitedPages.size());
                crawlingController.sendPagesCrawled(visitedPages.size());
                try {
                    crawlingController.sendCrawlingLog("Crawling [" + link + "]");
                    Connection.Response response = Jsoup.connect(link)
                            .timeout(2 * 1000)
                            .ignoreHttpErrors(true)
                            .execute();
                    if (response.statusCode() != 200) {
                        crawlingController.sendCrawlingLog("Link responded with invalid status code. Skipping");
                        continue;
                    }
                    String contentType = response.contentType();
                    if (!contentType.startsWith("text/html")) {
                        crawlingController.sendCrawlingLog("Page format not HTML. Skipping");
                        continue;

                    }
                    if (link.contains("#")) {
                        crawlingController.sendCrawlingLog("Link references to itself. Skipping");
                        continue;
                    }
                    System.out.println(link);
                    Document document = response.parse();
                    if (document.body() == null) {
                        crawlingController.sendCrawlingLog("Link has no body. Skipping");
                        continue;
                    }
                    Integer[] occurrences = findOccurrences(terms, document);
                    seedOccurrences.put(link, occurrences);
                    newLinks.addAll(getPageLinks(document));
                } catch (SocketTimeoutException e) {
                    String message = "Timout of connecting to the page [" + link + "]";
                    crawlingController.sendCrawlingLog(message);
                    e.printStackTrace();
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
                } finally {
                    visitedPages.add(link);
                }
            }
            links.addAll(newLinks);
            newLinks.clear();
        }

//        seedOccurrences = new LinkedHashMap<>(tempSeedOccurrences);
        for (Map.Entry<String, Integer[]> entry : seedOccurrences.entrySet()) {
            System.out.print(entry.getKey() + " ");
            for (Integer hit : entry.getValue()) {
                System.out.print(hit + " ");
            }
            System.out.println();
        }
        links.clear();
        visitedPages.clear();
        newLinks.clear();
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

    private Set<String> getPageLinks(Document document) throws IOException {
        Set<String> links = new LinkedHashSet<>();
        Elements linksOnPage = document.select("a[href]");
        for (Element linkOnPage : linksOnPage) {
            String linkString = linkOnPage.attr("abs:href");
            links.add(linkString);
        }
        return links;
    }

}