package com.mablab;

import com.mablab.constants.Constants;
import com.mablab.data.processing.DataProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class to perform limited memory sort on a large file.
 */
@Slf4j
public class LimitedMemorySort {

    /**
     * Entry point of the program.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        LimitedMemorySort limitedMemorySort = new LimitedMemorySort();
        limitedMemorySort.sort();
    }

    /**
     * Sorts the input file using limited memory sort algorithm.
     */
    public void sort() {
        log.info("Starting the sort process...");
        Map<String, String> buffer = new TreeMap<>();
        List<String> tempFiles = new ArrayList<>();

        DataProcessor dataProcessor = new DataProcessor();

        dataProcessor.readAndSort(Constants.INPUT_FILE, buffer, tempFiles, Constants.BUFFER_SIZE);
        dataProcessor.mergeSortedFiles(tempFiles, Constants.OUTPUT_FILE);
        dataProcessor.deleteTempFiles(tempFiles);
        log.info("The sort process is finished.");
    }
}