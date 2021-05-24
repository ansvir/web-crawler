package com.project.webcrawler.controller;

import com.project.webcrawler.websockets.CrawlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
public class CrawlingController {

    private SimpMessagingTemplate simpMessagingTemplate;

    private boolean stopRequest;

    @PostConstruct
    public void postConstruct() {
        stopRequest = false;
    }

    @Autowired
    public CrawlingController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/stop")
    public void stopCrawling() {
        this.stopRequest = true;
    }

    /**
     * This method is used to send message to subscriber for specified URL
     * @param message Message to be sent
     */
    public void sendCrawlingLog(String message) {
        simpMessagingTemplate.convertAndSend("/crawling/log", new CrawlMessage(message));
    }

    public void sendPagesCrawled(int pagesCrawled) {
        simpMessagingTemplate.convertAndSend("/crawling/pages", new CrawlMessage(pagesCrawled, "pages"));
    }

    public void sendCurrentDepth(int currentDepth) {
        simpMessagingTemplate.convertAndSend("/crawling/depth", new CrawlMessage(currentDepth, "depth"));
    }

    public boolean getStopRequest() {
        return this.stopRequest;
    }

    public void setStopRequest(boolean stopRequest) {
        this.stopRequest = stopRequest;
    }
}
