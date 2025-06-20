package app.AndroidDriverCheck;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenRecording{
    public static void start(AndroidDriver driver) throws InterruptedException, IOException {

        // Start screen recording
        driver.startRecordingScreen(new AndroidStartScreenRecordingOptions()
                .withTimeLimit(Duration.ofMinutes(5))
                .withVideoSize("1280x720")
                .withBitRate(3000000));

        // Perform some actions (example)
         // Simulate some activity

    }
    public static void stop(AndroidDriver driver) throws IOException {
        String videoBase64 = driver.stopRecordingScreen();
        saveVideoToFile(videoBase64, "recording.mp4");
    }

    // Helper method to save Base64 video to a file
    public static void saveVideoToFile(String videoBase64, String filePath) throws IOException {
        byte[] videoBytes = Base64.getDecoder().decode(videoBase64);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(videoBytes);
            System.out.println("Video saved at: " + filePath);
        }
    }
}

