package Actions;

import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import io.appium.java_client.AppiumDriver;

public class Kill {
	public static AppiumDriver driver;
	public static void App(AppiumDriver driver) {
		Tap.withPercentage(driver, 0.28, 1.05);
		sleep(2000);
		Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
		int startY = (int) (size.getHeight() * 0.50);
		int startX = (int) (size.getWidth() * 0.50); // Adjusted to swipe left
		int endY = (int) (size.getWidth() * 0.15); // Adjusted to swipe left

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1)
				.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
				.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
				.addAction(new Pause(finger, Duration.ofMillis(150)))
				.addAction(
						finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), startX, endY))
				.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		driver.perform(Collections.singletonList(sequence));
	}
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
