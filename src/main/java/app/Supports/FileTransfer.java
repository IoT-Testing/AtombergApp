package app.Supports;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTransfer {
	public static void main(String[] args) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String mobileFilePath = "/sdcard/demo" + timestamp + ".mp4";
		String laptopFilePath = "C:\\Users\\Rohit\\Desktop\\Rohit\\Appium Screenshots\\NEWMOBILES\\Trail1\\Screenshot_"
				+ timestamp + ".mp4";

		try (FileInputStream mobileFileInput = new FileInputStream(mobileFilePath);
				FileOutputStream laptopFileOutput = new FileOutputStream(laptopFilePath)) {

			byte[] buffer = new byte[1024];
			int length;
			while ((length = mobileFileInput.read(buffer)) > 0) {
				laptopFileOutput.write(buffer, 0, length);
			}

			System.out.println("File transferred successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}