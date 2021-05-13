package com.project.webcrawler.websockets;

public class StopRequestException extends RuntimeException {
    public StopRequestException() {}
    public StopRequestException(String errorMessage) {
        super(errorMessage);
    }
}
