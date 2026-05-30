package cpit305.fcit.kau.edu.sa;

/**
 * Student Name: Mohammed Al-Dhani (محمد الضاني)
 * Student ID: 2336374
 * Course: CPIT-305 (Advanced Programming)
 * Assignment: Lab 5 - Java Threads and Word Counting
 */

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicLong;

public class MultiThreadedWordCounter {

    private static String readFileContent(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath));
    }

    private static long countWordsInText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    public static long countWords(String filePath, int numThreads) throws IOException, InterruptedException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        if (numThreads <= 0) {
            throw new IllegalArgumentException("Number of threads must be positive");
        }

        String content = readFileContent(filePath);
        String[] lines = content.split("\r?\n");
        int totalLines = lines.length;
        
        if (totalLines == 0) return 0;

        Thread[] threads = new Thread[numThreads];
        AtomicLong totalWordCount = new AtomicLong(0);
        int chunkSize = (int) Math.ceil((double) totalLines / numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, totalLines);

            if (start >= totalLines) break;

            threads[i] = new Thread(() -> {
                long localCount = 0;
                for (int j = start; j < end; j++) {
                    localCount += countWordsInText(lines[j]);
                }
                totalWordCount.addAndGet(localCount);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            if (thread != null) {
                thread.join();
            }
        }

        return totalWordCount.get();
    }

    public static long[] countWordsInFiles(String[] filePaths) throws IOException, InterruptedException {
        if (filePaths == null) {
            throw new IllegalArgumentException("File paths array cannot be null");
        }

        long[] wordCounts = new long[filePaths.length];
        Thread[] threads = new Thread[filePaths.length];

        for (int i = 0; i < filePaths.length; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    wordCounts[index] = countWords(filePaths[index], 2);
                } catch (Exception e) {
                    wordCounts[index] = 0;
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            if (thread != null) {
                thread.join();
            }
        }

        return wordCounts;
    }
}
