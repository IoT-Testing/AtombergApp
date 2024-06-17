package Actions;

import java.time.Duration;
import java.util.Collections;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import io.appium.java_client.AppiumDriver;

public class Swipe {
	public static void Left(AppiumDriver driver, double x, double y) {
		Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
		int startY = (int) (size.getHeight() * y);
		int startX = (int) (size.getWidth() * x); // Adjusted to swipe left
		int endX = (int) (size.getWidth() * 0.50); // Adjusted to swipe left

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1)
				.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
				.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
				.addAction(new Pause(finger, Duration.ofMillis(150)))
				.addAction(
						finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), endX, startY))
				.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		driver.perform(Collections.singletonList(sequence));
		System.out.println("Left Swipe");
	}

	public static void Right(AppiumDriver driver, double x, double y) {
		Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
		int startY = (int) (size.getHeight() * y);
		int startX = (int) (size.getWidth() * x); // Adjusted to swipe right
		int endX = (int) (size.getWidth() * 0.50); // Adjusted to swipe right

		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1)
				.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
				.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
				.addAction(new Pause(finger, Duration.ofMillis(150)))
				.addAction(
						finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), endX, startY))
				.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		driver.perform(Collections.singletonList(sequence));
		System.out.println("Right Swipe");
	}

	public static void screenRight(AppiumDriver driver) {
		Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size

		int startY = (int) (size.getHeight() * 0.5);
		int startX = (int) (size.getWidth() * 0.9); // Adjusted to swipe right
		int endX = (int) (size.getWidth() * 0.50); // Adjusted to swipe right
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1)
				.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
				.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
				.addAction(new Pause(finger, Duration.ofMillis(150)))
				.addAction(
						finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), endX, startY))
				.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
		driver.perform(Collections.singletonList(sequence));
		System.out.println("Right Swipe");
	}

	public static void screenLeft(AppiumDriver driver) {
		Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size

		int startY = (int) (size.getHeight() * 0.5);
		int startX = (int) (size.getWidth() * 0.1); // Adjusted to swipe right
		int endX = (int) (size.getWidth() * 0.50); // Adjusted to swipe left
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
		Sequence sequence = new Sequence(finger, 1)
				.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
				.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
				.addAction(new Pause(finger, Duration.ofMillis(150)))
				.addAction(
						finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), endX, startY))
				.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
		driver.perform(Collections.singletonList(sequence));
		System.out.println("Left Swipe");
	}

}
