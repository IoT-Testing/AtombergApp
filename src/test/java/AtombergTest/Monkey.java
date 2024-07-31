package AtombergTest; //To check 

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import Actions.Tap;

import java.time.Duration;
import java.util.Collections;
import java.util.Random;

public class Monkey {
// this is for random taps and swipes on the screen to check if the app crashes with the randomness

    public static void Run(AppiumDriver driver) {
        System.out.println("Monkey: count=200\r\n" + "AllowPackage: com.atomberg.app");
        for (int i = 1; i < 100; i++) {
            switch (Case()) {
                case 0:
                    tap(driver);
                    break;
                case 1:
                    ScrollUp(driver);
                    break;
                case 2:
                    ScrollDown(driver);
                    break;
                case 3:
                    swipeToRight(driver);
                    break;
                case 4:
                    swipeToLeft(driver);
                    break;
                case 5:
                    Minimize(driver);
                    break;
                case 6:
                    Back(driver);
                    break;
                case 7:
                    Check(driver);
                    break;
                case 8:
                    MinimizeandOpen(driver);
                    break;
                case 9:
                    killApp(driver);
                    break;
                case 10:
                    OpenApp(driver);
                    break;
            }
        }
        System.out.println("// Monkey finished ");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void tap(AppiumDriver driver) {
        int x = new Random().nextInt(1080);
        int y = new Random().nextInt(2460);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(150)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
        System.out.println("Tap with Coordinates");
        sleep(100);
    }

    public static void ScrollUp(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
        double x1 = 0.1 + (new Random().nextDouble()) * 0.8;
        double y1 = 0.1 + (new Random().nextDouble()) * 0.8;
        double x2 = 0.1 + (new Random().nextDouble()) * 0.8;
        double y2 = 0.1 + (new Random().nextDouble()) * 0.8;
        int startX = (int) (size.getHeight() * x1);
        int startY = (int) (size.getHeight() * y1);
        int endX = (int) (size.getHeight() * x2);
        int endY = (int) (size.getHeight() * y2); // Adjusted to scroll down
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(350), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
        System.out.println("Scrolled Up");
    }

    public static void ScrollDown(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
        double x1 = 0.1 + (new Random().nextDouble()) * 0.8;
        double y1 = 0.1 + (new Random().nextDouble()) * 0.8;
        double x2 = 0.1 + (new Random().nextDouble()) * 0.8;
        double y2 = 0.1 + (new Random().nextDouble()) * 0.8;
        int startX = (int) (size.getHeight() * x1);
        int startY = (int) (size.getHeight() * y1);
        int endX = (int) (size.getHeight() * x2);
        int endY = (int) (size.getHeight() * y2); // Adjusted to scroll down
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

    public static void swipeToRight(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
        double x = 0.1 + (new Random().nextDouble()) * 0.8;
        double y = 0.1 + (new Random().nextDouble()) * 0.8;
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

    public static void swipeToLeft(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size
        double x = 0.1 + (new Random().nextDouble()) * 0.8;
        double y = 0.1 + (new Random().nextDouble()) * 0.8;
        int startY = (int) (size.getHeight() * y);
        int startX = (int) (size.getWidth() * x); // Adjusted to swipe right
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

    public static void MinimizeandOpen(AppiumDriver driver) {
        Tap.withPercentage(driver, 0.28, 1.05);
        sleep(500);
        Tap.withPercentage(driver, 0.28, 1.05);

    }

    public static void Minimize(AppiumDriver driver) {
        Tap.withPercentage(driver, 0.28, 1.05);
    }

    public static void Back(AppiumDriver driver) {

        driver.navigate().back();
        sleep(100);

        WebElement App = null;
        try {
            App = driver.findElement(By.className("android.widget.ImageView"));
        } catch (Exception e) {
        }

        if (App == null) {
            Minimize(driver);
        }
    }

    public static void Check(AppiumDriver driver) {

        WebElement App = null;
        try {
            App = driver.findElement(By.className("android.widget.ImageView"));
        } catch (Exception e) {
        }

        if (App == null) {
            OpenApp(driver);
        }
    }

    public static void killApp(AppiumDriver driver) {
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

    public static void OpenApp(AppiumDriver driver) {
        Tap.withPercentage(driver, 0.50, 1.05);
        WebElement App = null;
        try {
            App = driver.findElement(By.xpath("//android.widget.TextView[@content-desc=\"Atomberg Home\"]"));
        } catch (Exception e) {
        }
        if (App == null) {
            Tap.withPercentage(driver, 0.50, 1.05);
        } else {
            App.click();
        }
    }

    public static int Case() {
        Random random = new Random();
        int randomNumber = random.nextInt(100); // generate a random number between 0 and 99
        if (randomNumber < 45) {
            return 0;
        } else if (randomNumber < 50) {
            return 1;
        } else if (randomNumber < 55) {
            return 2;
        } else if (randomNumber < 60) {
            return 3;
        } else if (randomNumber < 65) {
            return 4;
        } else if (randomNumber < 70) {
            return 5;
        } else if (randomNumber < 75) {
            return 6;
        } else if (randomNumber < 80) {
            return 7;
        } else if (randomNumber < 85) {
            return 8;
        } else if (randomNumber < 90) {
            return 9;
        } else {
            return 10;
        }

    }

}
