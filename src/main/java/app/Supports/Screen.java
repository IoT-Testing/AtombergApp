package app.Supports; //To check

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Screen {
	public static Process process = null;


    private Screen(Process process) {
        Screen.process = process;
    }

    public static void recordStart() throws IOException, InterruptedException {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String[] command = { "adb", "shell", "screenrecord", "/sdcard/Download/" + timestamp + ".mp4" };
		process = new ProcessBuilder(command).start();
		logpoint("Screen recording started. Press Ctrl+C to stop the recording.");
		// adb shell screenrecord /sdcard/demo.mp4
	}
	public static void recordStop(){
		process.destroy();
		logpoint("Stop Recording");
	}

}
