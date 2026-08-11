package app.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.*;

import java.time.Duration;
import java.util.Collections;

/**
 * ActionsUtil â€“ reusable touch/gesture interactions for Appium Android.
 *
 * <p>Provides three inner utility classes ({@link Tap}, {@link Scroll},
 * {@link Swipe}) and top-level helpers ({@link #longPress}, {@link #killApp},
 * {@link #refresh}, {@link #minimize}, {@link #sleep}, {@link #SSleep}) so
 * that callers never have to construct W3C {@code Sequence} objects directly.</p>
 */
public class ActionsUtil {

    // â”€â”€ Tap â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Tap utilities: coordinate, percentage, element-center, and connect-button.
     */
    public static class Tap {

        /** Taps at exact screen pixel coordinates. */
        public static void withCoordinates(AndroidDriver driver, int x, int y) {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(150)))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(tap));
            logpoint("Tap at (" + x + ", " + y + ")");
        }

        /** Taps at a position expressed as ratios (0.0â€“1.0) of the screen dimensions. */
        public static void withPercentage(AndroidDriver driver, double xRatio, double yRatio) {
            Dimension size = driver.manage().window().getSize();
            int x = (int) (size.getWidth()  * xRatio);
            int y = (int) (size.getHeight() * yRatio);
            withCoordinates(driver, x, y);
        }

        /** Taps the visual center of a given element. */
        public static void element(AndroidDriver driver, WebElement element) {
            withCoordinates(driver, getElementCenterX(element), getElementCenterY(element));
        }

        /**
         * Finds the first visible "Connect" button on screen and taps at the same
         * vertical centre as {@code targetElement} â€” useful for rows in a device list.
         */
        public static void connectButton(AndroidDriver driver, WebElement targetElement) {
            int midY = getElementCenterY(targetElement);
            try {
                WebElement connectBtn = driver.findElement(
                        By.xpath("(//android.view.View[@content-desc='Connect'])[1]"));
                withCoordinates(driver, getElementCenterX(connectBtn), midY);
            } catch (Exception e) {
                System.err.println("Failed to locate 'Connect' button: " + e.getMessage());
            }
        }

        // Package-accessible so sibling nested classes (Scroll) can reuse them
        static int getElementCenterX(WebElement el) {
            return el.getLocation().getX() + (el.getSize().getWidth()  / 2);
        }

        static int getElementCenterY(WebElement el) {
            return el.getLocation().getY() + (el.getSize().getHeight() / 2);
        }
    }

    // â”€â”€ Scroll â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Vertical scroll utilities: up, down, slow, and within-element.
     */
    public static class Scroll {

        private static final double SCROLL_START_Y_RATIO  = 0.5;
        private static final double SCROLL_UP_END_RATIO   = 0.20;
        private static final double SCROLL_DOWN_END_RATIO = 0.80;
        private static final long   DEFAULT_DURATION_MS   = 250;

        /** Scrolls up from mid-screen to near the top. */
        public static void Up(AndroidDriver driver) {
            performScroll(driver, SCROLL_START_Y_RATIO, SCROLL_UP_END_RATIO, DEFAULT_DURATION_MS);
            logpoint("Scrolled Up");
        }

        /** Scrolls down from mid-screen toward the bottom. */
        public static void Down(AndroidDriver driver) {
            performScroll(driver, SCROLL_START_Y_RATIO, SCROLL_DOWN_END_RATIO, DEFAULT_DURATION_MS);
            logpoint("Scrolled Down");
        }

        /** Slower scroll up â€” useful for content that needs time to load. */
        public static void slowUp(AndroidDriver driver) {
            performScroll(driver, SCROLL_START_Y_RATIO, SCROLL_UP_END_RATIO, 500);
            logpoint("Slowly Scrolled Up");
        }

        /**
         * Scrolls within the bounds of a specific element (e.g. a scrollable list widget).
         * Starts at the bottom edge of the element and drags toward its top.
         */
        public static void element(AndroidDriver driver, WebElement element) {
            int centerX = Tap.getElementCenterX(element);
            int startY  = element.getLocation().getY() + element.getSize().getHeight();
            int endY    = element.getLocation().getY()
                    + (int) (element.getSize().getHeight() * 0.2);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(200)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(250), PointerInput.Origin.viewport(), centerX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(scroll));
            logpoint("Scrolled inside element");
        }

        private static void performScroll(AndroidDriver driver,
                                          double startRatio, double endRatio, long durationMs) {
            Dimension size = driver.manage().window().getSize();
            int startX = size.getWidth()  / 2;
            int startY = (int) (size.getHeight() * startRatio);
            int endY   = (int) (size.getHeight() * endRatio);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence sequence = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(200)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), startX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(sequence));
        }
    }

    // â”€â”€ Swipe â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Horizontal swipe and notification-panel utilities.
     */
    public static class Swipe {

        private static final int FLING_DURATION_MS = 150;

        /**
         * Swipes left starting at {@code startXRatio} and landing at 50 % of screen width.
         *
         * @param startXRatio horizontal start position (0.0â€“1.0)
         * @param yRatio      vertical position          (0.0â€“1.0)
         */
        public static void Left(AndroidDriver driver, double startXRatio, double yRatio) {
            performHorizontalSwipe(driver, startXRatio, yRatio, 0.50, FLING_DURATION_MS);
            logpoint("Swiped Left");
        }

        /**
         * Swipes right starting at {@code startXRatio} and landing at 50 % of screen width.
         *
         * @param startXRatio horizontal start position (0.0â€“1.0)
         * @param yRatio      vertical position          (0.0â€“1.0)
         */
        public static void Right(AndroidDriver driver, double startXRatio, double yRatio) {
            performHorizontalSwipe(driver, startXRatio, yRatio, 0.50, FLING_DURATION_MS);
            logpoint("Swiped Right");
        }

        /** Swipes from the left edge toward the right (screen-level gesture). */
        public static void screenLeft(AndroidDriver driver) {
            performHorizontalSwipe(driver, 0.1, 0.5, 0.9, FLING_DURATION_MS);
            logpoint("Screen Swiped Left");
        }

        /** Swipes from the right edge toward the left (screen-level gesture). */
        public static void screenRight(AndroidDriver driver) {
            performHorizontalSwipe(driver, 0.9, 0.5, 0.1, FLING_DURATION_MS);
            logpoint("Screen Swiped Right");
        }

        /**
         * Pulls down the notification shade by swiping from the status bar downward.
         *
         * @param xRatio       horizontal touch position   (0.0â€“1.0)
         * @param startYRatio  starting Y position â€” use a value near 0.0 for the status bar
         */
        public static void Notifications(AndroidDriver driver, double xRatio, double startYRatio) {
            performVerticalSwipe(driver, xRatio, startYRatio, 0.50, FLING_DURATION_MS);
            logpoint("Pulled down notifications");
        }

        private static void performHorizontalSwipe(AndroidDriver driver,
                                                   double startXRatio, double yRatio,
                                                   double endXRatio, long durationMs) {
            Dimension size = driver.manage().window().getSize();
            int startX = (int) (size.getWidth()  * startXRatio);
            int y      = (int) (size.getHeight() * yRatio);
            int endX   = (int) (size.getWidth()  * endXRatio);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, y))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(150)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, y))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Collections.singletonList(swipe));
        }

        private static void performVerticalSwipe(AndroidDriver driver,
                                                 double xRatio, double startYRatio,
                                                 double endYRatio, long durationMs) {
            Dimension size = driver.manage().window().getSize();
            int x      = (int) (size.getWidth()  * xRatio);
            int startY = (int) (size.getHeight() * startYRatio);
            int endY   = (int) (size.getHeight() * endYRatio);

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

    // â”€â”€ Pull-to-refresh â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /** Performs a standard pull-to-refresh gesture from mid-screen downward. */
    public static void refresh(AndroidDriver driver) {
        Dimension size  = driver.manage().window().getSize();
        int centerX = size.getWidth()  / 2;
        int startY  = (int) (size.getHeight() * 0.35);
        int endY    = (int) (size.getHeight() * 0.60);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence seq = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), centerX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(seq));
        logpoint("Pull-to-refresh performed");
    }

    // â”€â”€ Long press â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Performs a 2-second long press at the given screen coordinates.
     *
     * @param x X pixel coordinate
     * @param y Y pixel coordinate
     */
    public static void longPress(AndroidDriver driver, int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPressSeq = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofSeconds(2)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(longPressSeq));
        logpoint("Long press at (" + x + ", " + y + ")");
    }

    // â”€â”€ Minimize / Kill â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /** Taps the home soft-key area to minimize the app. */
    public static void minimize(AndroidDriver driver) {
        Tap.withPercentage(driver, 0.50, 1.05);
        sleep(2000);
    }

    /**
     * Opens the recent-apps list and swipes the foreground app card away to kill it.
     * Uses the Recents soft-key area (28 % from left, typical 3-button nav bar).
     */
    public static void killApp(AndroidDriver driver) {
        Tap.withPercentage(driver, 0.28, 1.05);
        sleep(2000);

        Dimension size  = driver.manage().window().getSize();
        int centerX = size.getWidth()  / 2;
        int startY  = (int) (size.getHeight() * 0.50);
        int endY    = (int) (size.getHeight() * 0.15);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipeAway = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(150)))
                .addAction(finger.createPointerMove(Duration.ofMillis(150), PointerInput.Origin.viewport(), centerX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(swipeAway));
        logpoint("App removed from recent tasks");
    }

    // â”€â”€ Sleep â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /** Pauses the current thread for {@code millis} milliseconds. */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrupted", e);
        }
    }

    /** Pauses the current thread for {@code seconds} seconds. */
    public static void SSleep(long seconds) {
        sleep(seconds * 1000);
    }
}
