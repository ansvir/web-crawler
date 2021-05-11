package com.project.webcrawler.logic;

public enum Constant {
    MAX_LINK_DEPTH(2), MAX_PAGES_VISITED(10000);

    private final int VALUE;

    Constant(int VALUE) {
        this.VALUE = VALUE;
    }

    public int getValue() {
        return this.VALUE;
    }
}
