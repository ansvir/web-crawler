package com.project.webcrawler.controller;

import com.project.webcrawler.websockets.CrawlLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CrawlingController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendCrawlingLog(String message) {
        simpMessagingTemplate.convertAndSend("/crawling/log", new CrawlLog(message));
    }
}
