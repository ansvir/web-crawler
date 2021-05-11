package com.project.webcrawler.logic;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.webcrawler.logic.Constant.MAX_LINK_DEPTH;
import static com.project.webcrawler.logic.Constant.MAX_PAGES_VISITED;

public class WebCrawler {
    private Set<String> links;
    private Set<String> newLinks;
    private Set<String> visitedPages;

    public WebCrawler() {
        links = new LinkedHashSet<>();
        newLinks = new LinkedHashSet<>();
        visitedPages = new HashSet<>();
    }

    public Map<String, Integer[]> crawl(String seed, String... terms) {
            Map<String, Integer[]> seedOccurrences = new LinkedHashMap<>();
            links.add(seed);
            for (int i = 0; i < MAX_LINK_DEPTH.getValue(); i++) {
                System.out.println("Depth: " + i);
                for (String link : links) {
                    if (visitedPages.contains(link)) {
                        continue;
                    }
                    if (visitedPages.size() >= MAX_PAGES_VISITED.getValue()) {
                        return seedOccurrences;
                    }
                    System.out.println(link);
                    try {
                        Connection.Response response = Jsoup.connect(link).execute();
                        String contentType = response.contentType();
                        System.out.println(contentType);
                        if (!contentType.startsWith("text/html")) {
                            continue;
                        }
                        Document document = response.parse();
                        Integer[] occurrences = findOccurrences(terms, document);
                        seedOccurrences.put(link, occurrences);
                        newLinks.addAll(getPageLinks(link));
                    } catch (IllegalArgumentException e) {
                        System.out.println("link [" + link + "] skipped according to");
                        e.printStackTrace();
                    } catch (UnsupportedMimeTypeException e) {
                        System.out.println("Unsupported mimetype for link [" + link + "]");
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        System.out.println("Unsupported protocol, link [" + link + "] skipped");
                        e.printStackTrace();
                    } catch (HttpStatusException e) {
                        System.out.println("Http status is incorrect to parse the page [" + link + "]");
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("IOException occurred");
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
        return seedOccurrences;
    }

    private Integer[] findOccurrences(String[] terms, Document document) {
        Integer[] occurrences = new Integer[terms.length+1];
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