package app.util;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

public class ActionsUtil {
    public static class Tap {
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

    public  static class Scroll{
        public static void Down(AppiumDriver driver) {
            Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
            int startX = size.getHeight() / 2;
            int startY = size.getHeight() / 2;
            int endX = size.getHeight() / 2;
            int endY = (int) (size.getHeight() * 0.80); // Adjusted to scroll down

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence sequence = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(200)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(250), PointerInput.Origin.viewport(), endX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(sequence));
            System.out.println("Scrolled Down");
        }

        public static void Up(AppiumDriver driver) {
            Dimension size = driver.manage().window().getSize();
            int startX = size.getWidth() / 2;
            int startY = size.getHeight() / 2;
            int endY = (int) (size.getHeight() * 0.30);
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence sequence = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(200)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), startX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(sequence));
            System.out.println("Scrolled Up");
        }
    }

    public static class Swipe {
        // Screen swipes
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

        public void screenRight(AppiumDriver driver) {
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

        public void screenLeft(AppiumDriver driver) {
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

    public static class Kill {
        // this is to minimize the app and kill the main activity of the app
        public void App(AppiumDriver driver) {
            Tap.withPercentage(driver, 0.28, 1.05);// Tap on the recent button on the navigation bar
            sleep(2000);
            Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
            int startY = (int) (size.getHeight() * 0.50);
            int startX = (int) (size.getWidth() * 0.50); // Adjusted to swipe left
            int endY = (int) (size.getWidth() * 0.15); // Adjusted to swipe left
            // To remove the app from the recent section
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
    }

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
