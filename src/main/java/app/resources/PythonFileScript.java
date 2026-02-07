package app.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonFileScript {
    public PythonFileScript(){}
    public void script(){
        try {
            // Replace "your_script.py" with the path to your Python file
            ProcessBuilder processBuilder = new ProcessBuilder("python", "d:/AtombergAppBoF/Ble_Sequence.py");
            Process process = processBuilder.start();

            // Read the output from the Python script
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            // Wait for the process to finish and get the exit code
//            int exitCode = process.waitFor();
//            System.out.println("Python script exited with code: " + exitCode);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
