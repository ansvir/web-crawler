package com.project.webcrawler.logic;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

class ValueTopComparator implements Comparator<String> {
    Map<String, Integer[]> base;

    public ValueTopComparator(Map<String, Integer[]> base) {
        this.base = base;
    }

    public int compare(String a, String b) {
        Set<Map.Entry<String, Integer[]>> statisticsSet = base.entrySet();
        Map.Entry<String, Integer[]> statisticsMap = statisticsSet.iterator().next();
        Integer[] statistics = statisticsMap.getValue();
        if (base.get(a)[statistics.length-1] >= base.get(b)[statistics.length-1]) {
            return -1;
        } else {
            return 1;
        }
    }
}