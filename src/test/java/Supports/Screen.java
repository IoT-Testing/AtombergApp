package Supports; //To check 

import io.appium.java_client.AppiumDriver;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import Login.Login;

public class Screen {
	public static void Record(AppiumDriver driver) throws IOException, InterruptedException {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String[] command = { "adb", "shell", "screenrecord", "/sdcard/demo" + timestamp + ".mp4" };
		Process process = new ProcessBuilder(command).start();
		System.out.println("Screen recording started. Press Ctrl+C to stop the recording.");

		Login.main(driver);
		System.out.println("Monkey Started....");
		driver.executeScript("mobile:shell", Map.of("command", "monkey", "args",
				"-p com.atomberg.app --throttle 100 -v --pct-touch 40 --pct-motion 35 --pct-nav 0 --pct-majornav 0 --pct-appswitch 5 --pct-anyevent 5 --pct-trackball 0 --pct-syskeys 0 --pct-pinchzoom 5 1000"));
		System.out.println("Monkey finished....");
		process.destroy();
		// adb shell screenrecord /sdcard/demo.mp4
	}

}