package com.project.webcrawler.websockets;

import lombok.Data;

@Data
public class CrawlMessage {
    private String content;

    public CrawlMessage() {}

    public CrawlMessage(String content) {
        this.content = content;
    }
}
