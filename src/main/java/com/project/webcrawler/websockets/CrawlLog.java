package com.project.webcrawler.websockets;

import lombok.Data;

@Data
public class CrawlLog {
    private String content;

    public CrawlLog() {}

    public CrawlLog(String content) {
        this.content = content;
    }
}
