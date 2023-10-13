package helper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffleLines {

    public static void main(String[] args) {
        try {
            File inputFile = new File("files/russian_nouns_with_definition.txt");
            File outputFile = new File("files/SHUFFLED_" + inputFile.getName());
            List<String> lines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            Collections.shuffle(lines);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
