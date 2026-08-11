package app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * ReadFromCSV - Safely reads email/password pairs from a CSV file.
 *
 * <p>Expected format: {@code email,password}
 *
 * <p>This version improves:
 * <ul>
 *   <li>Error visibility</li>
 *   <li>Resource safety</li>
 *   <li>Data validation</li>
 *   <li>Flexibility</li>
 * </ul>
 */
public class ReadFromCSV {

    // === Constants ===
    private static final String DEFAULT_CSV_PATH = System.getProperty("user.dir") + "/accounts.csv";
    private static final int EXPECTED_COLUMNS = 2;
    private static final String DELIMITER = ",";

    /**
     * Reads email/password pairs from default CSV file.
     *
     * @return List of credential pairs: [ [email, password], ... ]
     */
    public static List<List<String>> readFromCSV() {
        return readFromCSV(DEFAULT_CSV_PATH);
    }

    /**
     * Reads email/password pairs from specified CSV file.
     *
     * @param csvPath Path to CSV file
     * @return List of credential pairs
     * @throws IllegalArgumentException if file is missing or unreadable
     */
    public static List<List<String>> readFromCSV(String csvPath) {
        Objects.requireNonNull(csvPath, "CSV path cannot be null");

        Path path = Paths.get(csvPath);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("CSV file not found: " + csvPath);
        }
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("CSV file is not readable: " + csvPath);
        }

        List<List<String>> result = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }

                String[] values = line.split(DELIMITER, -1); // Preserve trailing empty fields

                if (values.length != EXPECTED_COLUMNS) {
                    System.err.printf("Skipping invalid line %d: expected %d columns but got %d%n", lineNumber, EXPECTED_COLUMNS, values.length);
                    continue;
                }

                String email = values[0].trim();
                String password = values[1].trim();

                if (email.isEmpty()) {
                    System.err.printf("Skipping line %d: email is empty%n", lineNumber);
                    continue;
                }
                if (password.isEmpty()) {
                    System.err.printf("Warning: password is empty for email '%s' (line %d)%n", email, lineNumber);
                }

                result.add(List.of(email, password));
            }

            logpoint("Successfully loaded " + result.size() + " account(s) from: " + csvPath);
            return result;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + csvPath, e);
        }
    }

    /**
     * Returns the default CSV path.
     */
    public static String getDefaultCsvPath() {
        return DEFAULT_CSV_PATH;
    }
}
