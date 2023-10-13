import java.io.*;
import java.util.*;

public class LimitedMemorySort {

    public static void main(String[] args) throws IOException {
        String inputFile = "files/SHUFFLED_russian_nouns_with_definition.txt";
        String outputFile = "files/sorted-output.txt";

        int bufferSize = 1000;

        Map<String, String> buffer = new TreeMap<>();
        List<String> tempFiles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int linesRead = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                buffer.put(parts[0], parts[1]);
                linesRead++;
                if (linesRead >= bufferSize) {
                    saveFile(buffer, tempFiles);
                    buffer.clear();
                    linesRead = 0;
                }
            }
            if (!buffer.isEmpty()) {
                saveFile(buffer, tempFiles);
            }
        }

        mergeSortedFiles(tempFiles, outputFile);

        for (String tempFile : tempFiles) {
            new File(tempFile).delete();
        }
    }

    private static void saveFile(Map<String, String> buffer, List<String> tempFiles) throws IOException {
        String tempFile = "files/temp_" + System.currentTimeMillis() + ".txt";
        tempFiles.add(tempFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (Map.Entry<String, String> entry : buffer.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
                writer.flush();
            }
        }
    }

    public static void mergeSortedFiles(List<String> fileNames, String outputFileName) throws IOException {
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
