package com.project.webcrawler.logic;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class CSVProcessor {

    /**
     * This method is used for generating CSV file that contains lines of statistics parameter
     *
     * @param statistics Map that contains links and according occurrences of term ordered by terms specified in CrawlRequest
     * @return Generated CSV file
     */
    public File createAllStatReport(Map<String, Integer[]> statistics) {
        File csvFile = new File("src/main/resources/files/report.csv");
        try (ByteArrayOutputStream csvFileBytes = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(csvFileBytes), CSVFormat.DEFAULT)) {
            statistics.forEach((link, statisticArray) -> {
                try {
                    Object[] printable = new Object[statisticArray.length + 1];
                    printable[0] = link;
                    for (int i = 1; i < printable.length; i++) {
                        printable[i] = statisticArray[i - 1];
                    }
                    printer.printRecord(printable);
                    printer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            FileUtils.writeByteArrayToFile(csvFile, csvFileBytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFile;
    }

    /**
     * This method is used for generating CSV file that contains top ten lines of statistics parameter and total occurrences for each line ordered descending
     *
     * @param statistics Map that contains links and according occurrences of term ordered by terms specified in CrawlRequest
     * @return Generated CSV file
     */
    public File createTopTenStatReport(Map<String, Integer[]> statistics) {
        ValueTopComparator vtc = new ValueTopComparator(statistics);
        Map<String, Integer[]> sortedStatistics = new TreeMap<>(vtc);
        sortedStatistics.putAll(statistics);
        Map<String, Integer[]> topTenStatistics = new LinkedHashMap<>();
        int counter = 0;
        for (Map.Entry<String, Integer[]> entry : sortedStatistics.entrySet()) {
            if (counter == 9) {
                break;
            }
            topTenStatistics.put(entry.getKey(), entry.getValue());
            counter++;
        }

        File csvFile = new File("src/main/resources/files/topTenReport.csv");
        try (ByteArrayOutputStream csvFileBytes = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(csvFileBytes), CSVFormat.DEFAULT)) {
            topTenStatistics.forEach((link, statisticArray) -> {
                try {
                    Object[] printable = new Object[statisticArray.length + 1];
                    printable[0] = link;
                    for (int i = 1; i < printable.length; i++) {
                        printable[i] = statisticArray[i - 1];
                    }
                    printer.printRecord(printable);
                    printer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            FileUtils.writeByteArrayToFile(csvFile, csvFileBytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFile;
    }
}
