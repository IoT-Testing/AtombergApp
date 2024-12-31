package Tabs;

import Actions.Scroll;
import io.appium.java_client.android.AndroidDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Automation {
    public static void TimeOfDay(AndroidDriver driver) {

        if (driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automations\"]")).isDisplayed()) {
            driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automations\"]")).click();
            System.out.println("Click on Automations");

            WebElement NewPlusButton = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Schedule actions\n" + "Example: Turn ON all bedroom fans at 11 PM\"]/android.widget.ImageView[1]"));
            if (NewPlusButton.isDisplayed()) {
                driver.findElement(By.xpath("//android.view.View[@content-desc=\"Schedule actions\nExample: Turn ON all bedroom fans at 11 PM\"]/android.widget.ImageView[1]")).click();
                newAutomation(driver);
            }

        }
    }

    public static void QuickAccess(AndroidDriver driver) {
        if (driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automations\"]")).isDisplayed()) {
            driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automations\"]")).click();
            System.out.println("Click on Automations");
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Quick access\"]")).click();
            driver.findElement(By.xpath("//android.view.View[@content-desc=\"Create 'Speed-dial' buttons for frequent actions\n" +
                    "Example: 'Goodbye' button to turn off all fans with one click\"]/android.view.View[5]")).click();

            WebElement QAName = driver.findElement(By.xpath("//android.widget.EditText"));
            QAName.click();
            String timestamp = new SimpleDateFormat("HHmmss").format(new Date());

            QAName.sendKeys("QA" + timestamp);
            selectRandomFan(driver);
            selectAction(driver);
            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Add\"]")).click();

            allScreenQA(driver);
        }
    }

    private static void newAutomation(AndroidDriver driver) {
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());

        WebElement AutoName = driver.findElement(By.xpath("//android.widget.EditText"));
        AutoName.click();
        AutoName.sendKeys("Automation" + timestamp);
        selectRandomFan(driver);
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select time\"]")).click();
        seekBar(driver);
        sleep(250);
        seekBar(driver);
        sleep(1000);
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Ok\"]")).click();
        selectAction(driver);
        Scroll.Up(driver);
        driver.findElement(By.xpath("//android.widget.Switch")).click();
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Add\"]")).click();

    }

    private static void selectRandomFan(AndroidDriver driver) {

        WebElement selectFan = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select fans\"]"));
        selectFan.click();
        List<WebElement> fans = driver.findElements(By.className("android.widget.CheckBox"));
        if (fans.size() == 1) {
            WebElement fan = fans.get(0);
            fan.click();
        }
        if (fans.size() > 1) {
            int x = new Random().nextInt(fans.size());
            WebElement fan = fans.get(x);
            fan.click();
        }
        WebElement Ok = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]"));
        Ok.click();
    }

    private static void seekBar(AndroidDriver driver) {
        Dimension size = driver.manage().window().getSize(); // Assuming getWindowSize() returns the window size

        int startY = (int) (size.getHeight() * 0.635);
        int startX = (int) (size.getWidth() * 0.21); // Adjusted to swipe right
        double x = 0.22 + (new Random().nextDouble()) * 0.8;
        int endX = (int) (size.getWidth() * x); // Adjusted to swipe right
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(150)))
                .addAction(
                        finger.createPointerMove(Duration.ofMillis(250), PointerInput.Origin.viewport(), endX, startY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
        System.out.println("SeekBar slide");
    }

    private static void selectAction(AndroidDriver driver) {
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Power ON\"]")).click();

        List<WebElement> ACTIONS = driver.findElements(By.className("android.view.View"));
        int x = new Random().nextInt(ACTIONS.size());
        WebElement action = ACTIONS.get(x);
        action.click();

    }

    private static void allScreenQA(AndroidDriver driver) {
        WebElement QA = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView"));
        QA.click();
        List<WebElement> ACTIONS = driver.findElements(By.className("android.widget.Button"));
        ACTIONS.remove(0);
        ACTIONS.remove(0);
        ACTIONS.remove(0);
        int i;
        int total = ACTIONS.size();
        for (i = 0; i < total; i++) {
            List<WebElement> Actions = driver.findElements(By.className("android.widget.Button"));
            Actions.remove(0);
            Actions.remove(0);
            Actions.remove(0);
            WebElement action = Actions.get(i);
            System.out.println(action.getDomAttribute("content-desc"));
            action.click();
            sleep(1000);
            if (i < total - 1) {
                QA.click();
            }
        }
    }

    public static void switchFamily(AndroidDriver driver) {
        List<WebElement> elements = driver.findElements(By.className("android.view.View"));
        WebElement e = elements.get(0);
        String fam1 = e.getDomAttribute("content-desc");
        System.out.println(e.getDomAttribute("content-desc"));

        e.click();
        // tap on the Family name on the screen (top right corner)

        // getting the availble family list
        List<WebElement> rawFamilies = driver.findElements(By.className("android.view.View"));
        List<WebElement> FAMILIES = rawFamilies.stream().filter(fam -> fam.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        FAMILIES.remove(FAMILIES.size() - 1);
        FAMILIES.remove(FAMILIES.size() - 1);
        int i;
        int total = FAMILIES.size();
        System.out.println("number of FAMILIES present =" + total);
        for (i = 0; i < total; i++) {
            List<WebElement> rawFamily = driver.findElements(By.className("android.view.View"));
            List<WebElement> families = rawFamily.stream().filter(fam -> fam.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println("number of families present =" + total);
            families.remove(families.size() - 1);
            families.remove(families.size() - 1);
            System.out.println("number of families1 present =" + total);
            if (families.get(i).getDomAttribute("content-desc").equals(fam1)) {
                i++;

            }
            System.out.println(families.get(i).getDomAttribute("content-desc") + " is clicked");
            families.get(i).click();

            sleep(1500);
            if (i < total - 1) {
                elements = driver.findElements(By.className("android.view.View"));
                e = elements.get(0);
                fam1 = e.getDomAttribute("content-desc");
                e.click();
            }
        }

    }

    public static void deleteAutomation(AndroidDriver driver) {
        WebElement ToD = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Automation155806\n" +
                "1 fan, 10:30 AM\n" +
                "Frequency\n" +
                "Daily\n" +
                "Activity\n" +
                "Power Toggle\"]"));
        String AutoName = ToD.getDomAttribute("content-desc");
        assert AutoName != null;
        boolean check = AutoName.startsWith("Automation");
        if (check) {
            ToD.click();
            WebElement delete = driver.findElement(By.xpath("//android.widget.ImageView/android.widget.Button"));
            delete.click();
            WebElement yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
            yes.click();
            assert driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automation Removed Successfully\"]")).isDisplayed();
        }

    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }

}