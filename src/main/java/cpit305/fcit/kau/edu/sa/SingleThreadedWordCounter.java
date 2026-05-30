package cpit305.fcit.kau.edu.sa;

/**
 * Student Name: Mohammed Al-Dhani (محمد الضاني)
 * Student ID: 2336374
 * Course: CPIT-305 (Advanced Programming)
 * Assignment: Lab 5 - Java Threads and Word Counting
 */

import java.io.*;
import java.nio.file.*;

public class SingleThreadedWordCounter {

    public static long countWords(String filePath) throws IOException, IllegalArgumentException {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        
        String content = Files.readString(Paths.get(filePath));
        return countWordsInText(content);
    }

    public static long[] countWordsInFiles(String[] filePaths) throws IOException, IllegalArgumentException {
        if (filePaths == null || filePaths.length == 0) {
            throw new IllegalArgumentException("File paths cannot be null or empty");
        }

        long[] wordCounts = new long[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            wordCounts[i] = countWords(filePaths[i]);
        }
        return wordCounts;
    }

    private static long countWordsInText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        String[] words = text.trim().split("\\s+");
        return words.length;
    }
}
