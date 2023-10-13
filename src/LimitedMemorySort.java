import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LimitedMemorySort {

    public static void main(String[] args) throws IOException {
        String inputFile = "files/input.txt";
        String outputFile = "files/sorted-output.txt";

        int bufferSize = 100;

        List<String> buffer = new ArrayList<>();
        List<String> tempFiles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int linesRead = 0;
            while ((line = reader.readLine()) != null) {
                buffer.add(line);
                linesRead++;
                if (linesRead >= bufferSize) {
                    Collections.sort(buffer);
                    String tempFile = "files/temp_" + System.currentTimeMillis() + ".txt";
                    tempFiles.add(tempFile);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                        for (String sortedLine : buffer) {
                            writer.write(sortedLine);
                            writer.newLine();
                        }
                    }
                    buffer.clear();
                    linesRead = 0;
                }
            }
            if (!buffer.isEmpty()) {
                Collections.sort(buffer);
                String tempFile = "files/temp_" + System.currentTimeMillis() + ".txt";
                tempFiles.add(tempFile);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                    for (String sortedLine : buffer) {
                        writer.write(sortedLine);
                        writer.newLine();
                    }
                }
            }
        }

        mergeSortedFiles(tempFiles, outputFile);

        for (String tempFile : tempFiles) {
            new File(tempFile).delete();
        }
    }

    private static void mergeSortedFiles(List<String> fileNames, String outputFileName) throws IOException {
        List<BufferedReader> readers = new ArrayList<>();
        for (String fileName : fileNames) {
            readers.add(new BufferedReader(new FileReader(fileName)));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            List<String> currentLines = new ArrayList<>(readers.size());

            for (int i = 0; i < readers.size(); i++) {
                String line = readers.get(i).readLine();
                if (line != null) {
                    currentLines.add(line);
                }
            }

            while (!currentLines.isEmpty()) {
                String minLine = Collections.min(currentLines);
                writer.write(minLine);
                writer.newLine();

                for(int i = 0; i < currentLines.size(); i++) {
                    if (currentLines.get(i).equals(minLine)) {
                        String nextLine = readers.get(i).readLine();
                        if(nextLine!=null){
                            currentLines.set(i, nextLine);
                        } else {
                            currentLines.remove(i);
                            readers.get(i).close();
                            i--;
                        }
                    }
                }
            }
        }
    }
}
