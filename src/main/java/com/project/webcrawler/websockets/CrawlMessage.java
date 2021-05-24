package com.project.webcrawler.websockets;

import lombok.Data;

@Data
public class CrawlMessage {
    private String content;
    private int pagesCrawled;
    private int currentDepth;

    public CrawlMessage(String content) {
        this.content = content;
    }

    public CrawlMessage(int value, String type) {
        if (type.equals("depth")) {
            this.currentDepth = value;
        } else if (type.equals("pages")) {
            this.pagesCrawled = value;
        }
    }
}
