package Tabs;

import Actions.Scroll;
import io.appium.java_client.ios.IOSDriver;
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
    public static void TimeOfDay(IOSDriver driver) {

        if (driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Automations\"]")).isDisplayed()) {
            driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Automations\"]")).click();
            System.out.println("Click on Automations");

            WebElement NewPlusButton = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Schedule actions\n" + "Example: Turn ON all bedroom fans at 11 PM\"]/XCUIElementTypeImage[1]"));
            if (NewPlusButton.isDisplayed()) {
                driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Schedule actions\nExample: Turn ON all bedroom fans at 11 PM\"]/XCUIElementTypeImage[1]")).click();
                newAutomation(driver);
            }

        }
    }

    public static void QuickAccess(IOSDriver driver) {
        if (driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Automations\"]")).isDisplayed()) {
            driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Automations\"]")).click();
            System.out.println("Click on Automations");
            driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Quick access\"]")).click();
            driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Create 'Speed-dial' buttons for frequent actions\n" +
                    "Example: 'Goodbye' button to turn off all fans with one click\"]/XCUIElementTypeStaticText[5]")).click();

            WebElement QAName = driver.findElement(By.xpath("//android.widget.EditText"));
            QAName.click();
            String timestamp = new SimpleDateFormat("HHmmss").format(new Date());

            QAName.sendKeys("QA" + timestamp);
            selectRandomFan(driver);
            selectAction(driver);
            driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Add\"]")).click();

            allScreenQA(driver);
        }
    }

    private static void newAutomation(IOSDriver driver) {
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());

        WebElement AutoName = driver.findElement(By.xpath("//android.widget.EditText"));
        AutoName.click();
        AutoName.sendKeys("Automation" + timestamp);
        selectRandomFan(driver);
        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Select time\"]")).click();
        seekBar(driver);
        sleep(250);
        seekBar(driver);
        sleep(1000);
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Ok\"]")).click();
        selectAction(driver);
        Scroll.Up(driver);
        driver.findElement(By.xpath("//android.widget.Switch")).click();
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Add\"]")).click();

    }

    private static void selectRandomFan(IOSDriver driver) {

        WebElement selectFan = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Select fans\"]"));
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
        WebElement Ok = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"OK\"]"));
        Ok.click();
    }

    private static void seekBar(IOSDriver driver) {
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

    private static void selectAction(IOSDriver driver) {
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Power ON\"]")).click();

        List<WebElement> ACTIONS = driver.findElements(By.className("XCUIElementTypeStaticText"));
        int x = new Random().nextInt(ACTIONS.size());
        WebElement action = ACTIONS.get(x);
        action.click();

    }

    private static void allScreenQA(IOSDriver driver) {
        WebElement QA = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeImage"));
        QA.click();
        List<WebElement> ACTIONS = driver.findElements(By.className("XCUIElementTypeButton"));
        ACTIONS.remove(0);
        ACTIONS.remove(0);
        ACTIONS.remove(0);
        int i;
        int total = ACTIONS.size();
        for (i = 0; i < total; i++) {
            List<WebElement> Actions = driver.findElements(By.className("XCUIElementTypeButton"));
            Actions.remove(0);
            Actions.remove(0);
            Actions.remove(0);
            WebElement action = Actions.get(i);
            System.out.println(action.getAttribute("name"));
            action.click();
            sleep(1000);
            if (i < total - 1) {
                QA.click();
            }
        }
    }

    public static void switchFamily(IOSDriver driver) {
        List<WebElement> elements = driver.findElements(By.className("XCUIElementTypeStaticText"));
        WebElement e = elements.get(0);
        String fam1 = e.getAttribute("name");
        System.out.println(e.getAttribute("name"));

        e.click();
        // tap on the Family name on the screen (top right corner)

        // getting the availble family list
        List<WebElement> rawFamilies = driver.findElements(By.className("XCUIElementTypeStaticText"));
        List<WebElement> FAMILIES = rawFamilies.stream().filter(fam -> fam.getAttribute("name") != null).collect(Collectors.toList());
        FAMILIES.remove(FAMILIES.size() - 1);
        int i;
        int total = FAMILIES.size();
        System.out.println("number of FAMILIES present =" + total);
        for (i = 0; i < total; i++) {
            List<WebElement> rawFamily = driver.findElements(By.className("XCUIElementTypeStaticText"));
            List<WebElement> families = rawFamily.stream().filter(fam -> fam.getAttribute("name") != null).collect(Collectors.toList());
//            System.out.println("number of families present =" + total);
//            families.remove(0);
//            families.remove(families.size() - 1);
//            System.out.println("number of families present =" + total);
//            if (families.get(i).getAttribute("name").equals(fam1)) {
//                i++;
//            }
//            System.out.println(families.get(i).getAttribute("name") + " is clicked");
//            families.get(i).click();
//
            for(WebElement ele: families){
                System.out.println(ele.getAttribute("name"));
            }
            sleep(1500);
            if (i < total - 1) {
                elements = driver.findElements(By.className("XCUIElementTypeStaticText"));
                e = elements.get(0);
                fam1 = e.getAttribute("name");
                e.click();
            }
        }

    }

    public static void deleteAutomation(IOSDriver driver) {
        WebElement ToD = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Automation155806\n" +
                "1 fan, 10:30 AM\n" +
                "Frequency\n" +
                "Daily\n" +
                "Activity\n" +
                "Power Toggle\"]"));
        String AutoName = ToD.getAttribute("name");
        boolean check = AutoName.startsWith("Automation");
        if (check) {
            ToD.click();
            WebElement delete = driver.findElement(By.xpath("//XCUIElementTypeImage/XCUIElementTypeButton"));
            delete.click();
            WebElement yes = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Yes\"]"));
            yes.click();
            assert driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Automation Removed Successfully\"]")).isDisplayed();
        }

    }


    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }

}