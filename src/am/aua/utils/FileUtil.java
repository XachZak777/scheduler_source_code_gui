package am.aua.utils;

import java.io.*;

public class FileUtil {

    public static void saveStringsToFile (String[] content, String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String line : content) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static String[] loadStringsFromFile (String path) throws IOException {
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        }
        String[] lines = new String[lineCount];
        int index = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines[index++] = line;
            }
        }
        return lines;
    }
}
