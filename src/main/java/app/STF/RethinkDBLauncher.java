package app.STF;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class RethinkDBLauncher {
    static String ip ;
    public static void main(String[] args) {
        try {
            List<String> command = new ArrayList<>();
            command.add("stf"); // Correct executable name
            command.add("local");
            command.add("--public-ip");
//            command.add(ip);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File("C:\\Users\\Rohit Bhagat\\AppData\\Roaming\\npm\\"));
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            System.out.println("RethinkDB output:\n" + output.toString());
            System.out.println("Exit Code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String networkip(){
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Local Hostname: " + localHost.getHostName());
            ip = localHost.getHostAddress();
            System.out.println("Local IP Address: " + localHost.getHostAddress());
        } catch (UnknownHostException e) {
            System.err.println("Could not find local host: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return ip;
    }
}

