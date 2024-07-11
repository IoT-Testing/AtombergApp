package Supports;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import AtombergTest.Method;
import Login.Email;
import io.appium.java_client.AppiumDriver;

public class Trail {
	public static int Array() {
		int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

		Random random = new Random();
		int randomIndex = random.nextInt(numbers.length);
        return numbers[randomIndex];
	}


}
