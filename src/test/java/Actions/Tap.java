package Actions;

import java.time.Duration;
import java.util.Collections;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import io.appium.java_client.AppiumDriver;


public class Tap {
	//2 types as there are some elements which are set w.r.t the screen dimensions of the mobile
	// tap with percentage is for elements with common locations in multiple devices
	// tap with co-ordinates is for the elements which might have slight different locations in different mobiles
	public static void withCoordinates(AppiumDriver driver, int x, int y) {
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1)
				.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
				.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
				.addAction(new Pause(finger, Duration.ofMillis(150)))
				.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
		driver.perform(Collections.singletonList(sequence));
		System.out.println("Tap with Coordinates");
	}

	public static void withPercentage(AppiumDriver driver, double x, double y) {
		Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
		int startY = (int) (size.getHeight() * y);
		int startX = (int) (size.getWidth() * x); // Adjusted to swipe left

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1)
				.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
				.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
				.addAction(new Pause(finger, Duration.ofMillis(150)))
				.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
		driver.perform(Collections.singletonList(sequence));
		System.out.println("Tap With %");
	}
}
