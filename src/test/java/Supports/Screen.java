package Supports; //To check 

import io.appium.java_client.android.AndroidDriver;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import Login.Login;

public class Screen {
	private static Process process = null;

    private Screen(Process process) {
        Screen.process = process;
    }

    public static void recordStart() throws IOException, InterruptedException {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String[] command = { "adb", "shell", "screenrecord", "/sdcard/Download/" + timestamp + ".mp4" };
		process = new ProcessBuilder(command).start();
		System.out.println("Screen recording started. Press Ctrl+C to stop the recording.");
		// adb shell screenrecord /sdcard/demo.mp4
	}
	public static void recordStop(){
		process.destroy();
		System.out.println("Stop Recording");
	}

}