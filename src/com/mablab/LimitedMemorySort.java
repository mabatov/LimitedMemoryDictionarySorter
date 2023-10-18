package com.mablab;

import com.mablab.data.processing.DataProcessor;
import com.mablab.constants.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class to perform limited memory sort on a large file.
 */
public class LimitedMemorySort {

    /**
     * Entry point of the program.
     *
     * @param args Command line arguments.
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        LimitedMemorySort limitedMemorySort = new LimitedMemorySort();
        limitedMemorySort.sort();
    }

    /**
     * Sorts the input file using limited memory sort algorithm.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void sort() throws IOException {
        Map<String, String> buffer = new TreeMap<>();
        List<String> tempFiles = new ArrayList<>();

        DataProcessor dataProcessor = new DataProcessor();

        dataProcessor.readAndSort(Constants.INPUT_FILE, buffer, tempFiles, Constants.BUFFER_SIZE);
        dataProcessor.mergeSortedFiles(tempFiles, Constants.OUTPUT_FILE);
        dataProcessor.deleteTempFiles(tempFiles);
    }
}