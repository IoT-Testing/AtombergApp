package app.util;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.time.Duration;
import java.util.Collections;

public class ActionsUtil {
    public AppiumDriver driver;
    public static class Tap {
        // TO perform a Tap action
        public static void withCoordinates(AppiumDriver driver, int x, int y) {
            //To perform tap action at the specified coordinates.
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
            //To perform tap action on an element without locator, and is located according to the screen of the device
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
    public static  void refresh(AppiumDriver driver){
        //Performs Refresh screen action.
        Dimension size = driver.manage().window().getSize();
        int startX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.35);
        int endY = (int) (size.getHeight() * 0.60);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), startX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(sequence));
        System.out.println("Home Screen Refreshed");
    }
    public  static class Scroll{
        //Performs Scroll actions
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
            int endY = (int) (size.getHeight() * 0.25);
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence sequence = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(200)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(250), PointerInput.Origin.viewport(), startX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(sequence));
            System.out.println("Scrolled Up");
        }
    }
    public static class Swipe {
        // Performs Screen swipes
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
    public static void minimize(AppiumDriver driver) {
        //Taps on the minimize button (button mode)
            Tap.withPercentage(driver, 0.50, 1.05);// Tap on the minimize button on the navigation bar
            sleep(2000);
    }
    public static void killApp(AppiumDriver driver) {
        // this is to minimize the app and kill the main activity of the app
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
                    .addAction(finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), startX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(sequence));
    }
    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void SSleep(long seconds){
        long millis = seconds*1000;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void longPress(AppiumDriver driver, int x, int y){
        //Performs Long Press action on the screen.
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
            .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
            .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
            .addAction(new Pause(finger, Duration.ofMillis(2000)))
            .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
        System.out.println("Tap with Coordinates");
    }

}
