package app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadFromCSV {
    public static List<List<String>> readFromCSV() {
        String csvPath = System.getProperty("user.dir") + "/accounts.csv";
        Path path = Paths.get(csvPath);
        BufferedReader reader = null;
        List<List<String>> result = new ArrayList<>();
        try {
            reader = Files.newBufferedReader(path);
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                String[] split = line.split(",");
                String email = split[0], password = split[1];
                result.add(List.of(email, password));
                line = reader.readLine();
            }
            System.out.println(result);
            return result;
        } catch (IOException e) {
            return new ArrayList<>();
        }

    }
}
