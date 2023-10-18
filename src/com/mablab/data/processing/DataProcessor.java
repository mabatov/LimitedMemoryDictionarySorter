package com.mablab.data.processing;

import com.mablab.constants.Constants;

import java.io.*;
import java.util.*;

/**
 * This class handles file operations.
 */
public class DataProcessor {

    /**
     * Reads the input file, sorts the data in memory, and saves it to temporary files.
     *
     * @param inputFile   The path of the input file.
     * @param buffer      The buffer to store the sorted data.
     * @param tempFiles   The list of temporary file names.
     * @param bufferSize  The maximum number of lines to be stored in memory.
     * @throws IOException If an I/O error occurs.
     */
    public void readAndSort(String inputFile, Map<String, String> buffer, List<String> tempFiles, int bufferSize) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int linesRead = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                buffer.put(parts[0], parts[1]);
                linesRead++;
                if (linesRead >= bufferSize) {
                    saveBufferToFile(buffer, tempFiles);
                    buffer.clear();
                    linesRead = 0;
                }
            }
            if (!buffer.isEmpty()) {
                saveBufferToFile(buffer, tempFiles);
            }
        }
    }

    private void saveBufferToFile(Map<String, String> buffer, List<String> tempFiles) throws IOException {
        String tempFile = Constants.TEMP_FILE_PREFIX + System.currentTimeMillis() + ".txt";
        tempFiles.add(tempFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (Map.Entry<String, String> entry : buffer.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        }
    }

    public void deleteTempFiles(List<String> fileNames) {
        for (String fileName : fileNames) {
            new File(fileName).delete();
        }
    }

    public void mergeSortedFiles(List<String> fileNames, String outputFileName) throws IOException {
        List<BufferedReader> readers = new ArrayList<>();
        PriorityQueue<String> heap = new PriorityQueue<>(Comparator.comparing(String::toString));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String fileName : fileNames) {
                readers.add(new BufferedReader(new FileReader(fileName)));
            }

            for (BufferedReader reader : readers) {
                String line = reader.readLine();
                if (line != null) {
                    heap.add(line);
                }
            }

            while (!heap.isEmpty()) {
                String min = heap.poll();
                writer.write(min);
                writer.newLine();

                for (BufferedReader reader : readers) {
                    String line = reader.readLine();
                    if (line != null) {
                        heap.add(line);
                    }
                }
            }
        }
    }
}