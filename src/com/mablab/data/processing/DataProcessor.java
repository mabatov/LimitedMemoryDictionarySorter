package com.mablab.data.processing;

import com.mablab.constants.Constants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

/**
 * This class handles file operations.
 */
@Slf4j
public class DataProcessor {

    /**
     * Reads the input file, sorts the data in memory, and saves it to temporary files.
     *
     * @param inputFile  The path of the input file.
     * @param buffer     The buffer to store the sorted data.
     * @param tempFiles  The list of temporary file names.
     * @param bufferSize The maximum number of lines to be stored in memory.
     */
    public void readAndSort(String inputFile, Map<String, String> buffer, List<String> tempFiles, int bufferSize) {
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
        } catch (IOException e) {
            log.error("Error occurred while reading and sorting the input file", e);
        }
    }

    /**
     * Saves the buffer data to a temporary file.
     *
     * @param buffer    The buffer containing the data to be saved.
     * @param tempFiles The list of temporary file names.
     */
    private void saveBufferToFile(Map<String, String> buffer, List<String> tempFiles) {
        String tempFile = Constants.TEMP_FILE_PREFIX + System.currentTimeMillis() + ".txt";
        tempFiles.add(tempFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (Map.Entry<String, String> entry : buffer.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Error occurred while saving buffer to file", e);
        }
    }

    /**
     * Deletes the temporary files.
     *
     * @param fileNames The list of temporary file names to be deleted.
     */
    public void deleteTempFiles(List<String> fileNames) {
        for (String fileName : fileNames) {
            boolean deleted = new File(fileName).delete();
            if (!deleted) {
                log.warn("Failed to delete temporary file: {}", fileName);
            }
        }
    }

    /**
     * Merges the sorted files into a single output file.
     *
     * @param fileNames      The list of sorted file names.
     * @param outputFileName The name of the output file.
     */
    public void mergeSortedFiles(List<String> fileNames, String outputFileName) {
        try {
            List<BufferedReader> readers = new ArrayList<>();
            PriorityQueue<LineWrapper> minHeap = new PriorityQueue<>(Comparator.comparing(LineWrapper::getLine));

            for (String inputFile : fileNames) {
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                readers.add(reader);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));

            for (BufferedReader reader : readers) {
                String line = reader.readLine();
                if (line != null) {
                    minHeap.offer(new LineWrapper(line, reader));
                }
            }

            while (!minHeap.isEmpty()) {
                LineWrapper minLine = minHeap.poll();
                String line = minLine.getLine();
                BufferedReader reader = minLine.getReader();

                writer.write(line);
                writer.newLine();

                line = reader.readLine();
                if (line != null) {
                    minHeap.offer(new LineWrapper(line, reader));
                }
            }

            for (BufferedReader reader : readers) {
                reader.close();
            }
            writer.close();
        } catch (IOException e) {
            log.error("An error occurred while merging sorted files", e);
        }
    }

    /**
     * Helper class to wrap a line with its corresponding reader
     */
    @Getter
    private static class LineWrapper {

        private final String line;
        private final BufferedReader reader;

        /**
         * Constructs a LineWrapper object with the given line and reader.
         *
         * @param line   the line of text
         * @param reader the BufferedReader associated with the line
         */
        public LineWrapper(String line, BufferedReader reader) {
            this.line = line;
            this.reader = reader;
        }

    }

}