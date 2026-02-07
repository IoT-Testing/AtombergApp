package app.Supports;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class AtombergFanStatus {

    public String fanStatus(String command) {
        try {
            // Command to run your python script (adjust path accordingly)
            JSONObject message = getJsonObject();
            JSONArray deviceStateArray = message.getJSONArray("device_state");
            JSONObject deviceState = deviceStateArray.getJSONObject(0);
            switch (command) {
                case "power" -> {
                    boolean powerState = deviceState.getBoolean("power");
                    return String.valueOf(powerState);
                }
                case "speed" -> {
                    int lastRecordedSpeed = deviceState.getInt("last_recorded_speed");
                    return String.valueOf(lastRecordedSpeed);
                }
                case "timer" -> {
                    int timerHours = deviceState.getInt("timer_hours");
                    return String.valueOf(timerHours);
                }
                case "led" ->{
                    boolean ledState = deviceState.getBoolean("led");
                    return String.valueOf(ledState);
                }
                case "sleep" -> {
                    boolean sleepState =  deviceState.getBoolean("sleep_mode");
                    return String.valueOf(sleepState);                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return null;
    }

    private static JSONObject getJsonObject() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python", "d:/AtombergAppBoF/src/main/java/app/api_tester.py");
        Process process = pb.start();

        // Read the python script output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line);
        }
        reader.close();

        // Assume output now contains the JSON response string
        String jsonResponse = output.toString();

        // Parse JSON and extract values
        JSONObject obj = new JSONObject(jsonResponse);
        JSONObject message = obj.getJSONObject("message");
        return message;
    }
}
