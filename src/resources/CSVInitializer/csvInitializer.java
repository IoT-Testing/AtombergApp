package resources.CSVInitializer;

public class CsvInitializer {
    public PrintWriter csvWriter;
    public boolean csvInitialized = false;
    public void initCSV(String testName) {
        if (csvInitialized) return;
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            csvWriter = new PrintWriter(new FileWriter(testName + timestamp + ".csv", true));
            csvWriter.println("Attempt Number,Action ID,Iteration,Status,Updated Version");
            csvWriter.flush();
            csvInitialized = true;
        } catch (IOException e) {
            System.err.println("Failed to create CSV file: " + e.getMessage());
        }
    }
    public void printRow(int attemptNumber, String status) {
        // Format: clean, no extra spaces
        String row = String.format("%d,%s",
                attemptNumber,status);

        // Write to CSV
        initCSV();
        csvWriter.println(row);
        csvWriter.flush();

        // Print to console
        System.out.printf("%-15d | %-8s%n",
                attemptNumber, status);
    }

}