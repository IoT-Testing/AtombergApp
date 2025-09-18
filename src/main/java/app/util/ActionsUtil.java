package app.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.*;
import java.time.Duration;
import java.util.Collections;


public class ActionsUtil {

    /**
     * Tap - Provides various tap actions.
     */
    public static class Tap {
        /**
         * Taps at specific screen coordinates.
         */
        public static void withCoordinates(AndroidDriver driver, int x, int y) {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(150)))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(tap));
//            System.out.println("Tap at (" + x + ", " + y + ")");
        }

        /**
         * Taps at a position defined by percentage of screen size.
         */
        public static void withPercentage(AndroidDriver driver, double xRatio, double yRatio) {
            Dimension size = driver.manage().window().getSize();
            int x = (int) (size.getWidth() * xRatio);
            int y = (int) (size.getHeight() * yRatio);
            withCoordinates(driver, x, y);
        }

        /**
         * Taps the center of a given element.
         */
        public static void element(AndroidDriver driver, WebElement element) {
            int midX = getElementCenterX(element);
            int midY = getElementCenterY(element);
            withCoordinates(driver, midX, midY);
        }

        /**
         * Specialized tap for Connect button relative to another element.
         */
        public static void connectButton(AndroidDriver driver, WebElement targetElement) {
            int midY = getElementCenterY(targetElement);

            // Find Connect button and calculate its horizontal center
            try {
                WebElement connectBtn = driver.findElement(By.xpath("(//android.view.View[@content-desc='Connect'])[1]"));
                int midX = getElementCenterX(connectBtn);
                withCoordinates(driver, midX, midY);
            } catch (Exception e) {
                System.err.println("Failed to locate 'Connect' button: " + e.getMessage());
            }
        }

        // === Internal Helpers ===
        private static int getElementCenterX(WebElement el) {
            return el.getLocation().getX() + (el.getSize().getWidth() / 2);
        }

        private static int getElementCenterY(WebElement el) {
            return el.getLocation().getY() + (el.getSize().getHeight() / 2);
        }
    }

    /**
     * Scroll - Vertical scrolling utilities.
     */
    public static class Scroll {
        private static final double SCROLL_START_Y_RATIO = 0.5;
        private static final double SCROLL_UP_END_RATIO = 0.20;
        private static final double SCROLL_DOWN_END_RATIO = 0.80;
        private static final long DEFAULT_DURATION_MS = 250;

        public static void Up(AndroidDriver driver) {
            performScroll(driver, SCROLL_START_Y_RATIO, SCROLL_UP_END_RATIO, DEFAULT_DURATION_MS);
            System.out.println("Scrolled Up");
        }

        public static void Down(AndroidDriver driver) {
            performScroll(driver, SCROLL_START_Y_RATIO, SCROLL_DOWN_END_RATIO, DEFAULT_DURATION_MS);
            System.out.println("Scrolled Down");
        }

        public static void slowUp(AndroidDriver driver) {
            performScroll(driver, SCROLL_START_Y_RATIO, SCROLL_UP_END_RATIO, 500);
            System.out.println("Slowly Scrolled Up");
        }

        /**
         * Scrolls within a specific element's bounds.
         */
        public static void element(AndroidDriver driver, WebElement element) {
            int centerX = Tap.getElementCenterX(element);
            int startY = getElementBottom(element);
            int endY = getElementTop(element) + (int) (element.getSize().getHeight() * 0.2);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(200)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(250), PointerInput.Origin.viewport(), centerX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(scroll));
            System.out.println("Scrolled inside element");
        }

        // === Internal Helpers ===
        private static void performScroll(AndroidDriver driver, double startRatio, double endRatio, long durationMs) {
            Dimension size = driver.manage().window().getSize();
            int startX = size.getWidth() / 2;
            int startY = (int) (size.getHeight() * startRatio);
            int endY = (int) (size.getHeight() * endRatio);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence sequence = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(200)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), startX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(sequence));
        }

        private static int getElementTop(WebElement el) {
            return el.getLocation().getY();
        }

        private static int getElementBottom(WebElement el) {
            return el.getLocation().getY() + el.getSize().getHeight();
        }
    }

    /**
     * Swipe - Horizontal swiping utilities.
     */
    public static class Swipe {
        private static final int FLING_DURATION_MS = 150;
        private static final int SLOW_SWIPE_DURATION_MS = 300;

        public static void Left(AndroidDriver driver, double startXRatio, double yRatio) {
            performSwipe(driver, startXRatio, yRatio, 0.50, FLING_DURATION_MS);
            System.out.println("Swiped Left");
        }

        public static void Right(AndroidDriver driver, double startXRatio, double yRatio) {
            performSwipe(driver, startXRatio, yRatio, 0.50, FLING_DURATION_MS);
            System.out.println("Swiped Right");
        }

        public static void screenLeft(AndroidDriver driver) {
            performSwipe(driver, 0.1, 0.5, 0.50, FLING_DURATION_MS);
            System.out.println("Screen Swiped Left");
        }

        public static void screenRight(AndroidDriver driver) {
            performSwipe(driver, 0.9, 0.5, 0.50, FLING_DURATION_MS);
            System.out.println("Screen Swiped Right");
        }

        public static void Notifications(AndroidDriver driver, double xRatio, double startYRatio) {
            performVerticalSwipe(driver, xRatio, startYRatio, 0.50, FLING_DURATION_MS);
            System.out.println("Pulled down notifications");
        }

        // === Internal Helpers ===
        private static void performSwipe(AndroidDriver driver, double startXRation, double yRatio, double endXRation, long durationMs) {
            Dimension size = driver.manage().window().getSize();
            int startX = (int) (size.getWidth() * startXRation);
            int y = (int) (size.getHeight() * yRatio);
            int endX = (int) (size.getWidth() * endXRation);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, y))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(150)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, y))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(swipe));
        }

        private static void performVerticalSwipe(AndroidDriver driver, double xRatio, double startYRatio, double endYRatio, long durationMs) {
            Dimension size = driver.manage().window().getSize();
            int x = (int) (size.getWidth() * xRatio);
            int startY = (int) (size.getHeight() * startYRatio);
            int endY = (int) (size.getHeight() * endYRatio);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(150)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), x, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(swipe));
        }
    }

    /**
     * Performs refresh gesture (pull-to-refresh).
     */
    public static void refresh(AndroidDriver driver) {
        Dimension size = driver.manage().window().getSize();
        int centerX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.35);
        int endY = (int) (size.getHeight() * 0.60);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence refresh = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), centerX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(refresh));
        System.out.println("Pull-to-refresh performed");
    }

    /**
     * Long press at coordinates.
     */
    public static void longPress(AndroidDriver driver, int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofSeconds(2)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(longPress));
        System.out.println("Long press at (" + x + ", " + y + ")");
    }

    /**
     * Minimizes app via navigation bar.
     */
    public static void minimize(AndroidDriver driver) {
        Tap.withPercentage(driver, 0.50, 1.05); // Bottom center
        sleep(2000);
    }

    /**
     * Closes app from recent apps list.
     */
    public static void killApp(AndroidDriver driver) {
        // Open recent apps
        Tap.withPercentage(driver, 0.28, 1.05);
        sleep(2000);

        Dimension size = driver.manage().window().getSize();
        int centerX = size.getWidth() / 2;
        int startY = (int) (size.getHeight() * 0.50);
        int endY = (int) (size.getHeight() * 0.15);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipeAway = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(150)))
                .addAction(finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), centerX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(swipeAway));

        System.out.println("App removed from recent tasks");
    }

    // === Sleep Utilities ===

    /**
     * Pauses thread for given milliseconds.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrupted", e);
        }
    }

    /**
     * Pauses thread for given seconds.
     */
    public static void SSleep(long seconds) {
        sleep(seconds * 1000);
    }
}