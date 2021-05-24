package com.project.webcrawler.websockets;

public class StopCrawlingException extends RuntimeException {
    public StopCrawlingException() {}
    public StopCrawlingException(String errorMessage) {
        super(errorMessage);
    }
}
