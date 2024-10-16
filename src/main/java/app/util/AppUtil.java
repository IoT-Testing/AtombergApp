package app.util;

import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class AppUtil {
    public static void captureScreenshot(AppiumDriver driver) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDirectory = System.getProperty("user.dir")+"\\screenshots\\";
        File screenshotFile = driver.getScreenshotAs(OutputType.FILE);
        String destinationFilePath = screenshotDirectory + "Screenshot_" + timestamp + ".png";
        try {
            FileUtils.copyFile(screenshotFile, new File(destinationFilePath));
            System.out.println("Appium screenshot saved as: " + destinationFilePath);
        } catch (IOException e) {
            System.out.println("Unable to save screenshot at " + destinationFilePath);
            System.out.println(e.getMessage());
        }
    }

    public static void SearchWiFi(AppiumDriver driver, String SearchString) {
        ActionsUtil.Tap.withPercentage(driver, 0.20, 0.20);
        //// android.widget.EditText[@text="Better_Together"]
        for (int i = 1; i <= 10; i++) {
            WebElement element = null;
            try {
                element = driver.findElement(By.xpath("//android.widget.EditText[@text=\""+SearchString+"\"]"));
            } catch (Exception ignored) {
            }
            if (element != null) {
                System.out.println(" " + SearchString + " Available");
                WebElement Password = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText[2]"));
                Password.getText();
                if (!Objects.equals(Password.getText(), "123@ToMb^rg#2425")) {
                    Password.clear();
                }
                Password.click();
                Password.sendKeys("123@ToMb^rg#2425");
                WebElement Continue = driver
                        .findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
                Continue.click();
                break;
            } else {
                WebElement WiFI = driver.findElement(By.xpath(
                        "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText[1]"));
                WiFI.click();
                WiFI.sendKeys("Better_Together");
                WebElement Password = driver.findElement(By.xpath(
                        "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText[2]"));
                Password.click();
                Password.sendKeys("987654321");
                WebElement Continue = driver
                        .findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
                Continue.click();
            }
        }
    }

    public static int Array() {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        Random random = new Random();
        int randomIndex = random.nextInt(numbers.length);
        return numbers[randomIndex];
    }

    public static class NumberPad {
        public void one(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,240,1725);
        }
        public void two(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,530,1725);
        }
        public void three(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,810,1725);
        }
        public void four(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,240,1945);
        }
        public void five(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,530,1945);
        }
        public void six(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,810,1945);
        }
        public void seven(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,240,2100);
        }
        public void eight(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,530,2100);
        }
        public void nine(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,810,2100);
        }
        public void zero(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,530,2250);
        }
        public void clear(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,240,2250);
        }
        public void done(AppiumDriver driver){
            ActionsUtil.Tap.withCoordinates(driver,810,2250);
        }
    }
}
