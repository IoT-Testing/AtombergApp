package app.Devices;

import app.util.ActionsUtil.Swipe;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class FanModels {

    // to select whether the selected model(at random) is renesa, renesa+. studio+
    public static void SixLEDColorSelect(AndroidDriver driver) {
        WebElement RPlus = null;
        WebElement SPlus = null;
        try {
            RPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Golden Oakwood\"]"));
        } catch (Exception e) {
            logpoint(e.getMessage());
        }
        try {
            SPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Earth Brown\"]"));
        } catch (Exception e) {
            logpoint(e.getMessage());
        }
        if (RPlus != null) {
            RenesaPlus(driver);
        } else if (SPlus != null) {
            StudioPlus(driver);
        } else {
            Renesa(driver);
        }

    }

    // below are the SKU according to the model selected
    public static void Renesa(AndroidDriver driver) {
        try {
            int randomNumber = (int) (Math.random() * 5); // generate a random number between 0 and 5
            switch (randomNumber) {
                case 0:
                    WebElement color1 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Brown and Black\"]"));
                    color1.click();
                    break;
                case 1:
                    WebElement color2 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"White and Black\"]"));
                    color2.click();
                    break;
                case 2:
                    Swipe.Right(driver, 0.9, 0.67);
                    WebElement color3 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Midnight Black\"]"));
                    color3.click();
                    break;
                case 3:
                    Swipe.Right(driver, 0.9, 0.67);
                    WebElement color4 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Pebble Grey\"]"));
                    color4.click();
                    break;
                case 4:
                    Swipe.Right(driver, 0.9, 0.67);
                    sleep(500);
                    Swipe.Right(driver, 0.9, 0.67);
                    WebElement color5 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Misty Teal\"]"));
                    color5.click();
                    break;

            }
        } catch (Exception exp) {
            logpoint(exp.getMessage());
        }
    }

    public static void RenesaPlus(AndroidDriver driver) {
        try {
            int randomNumber = (int) (Math.random() * 4); // generate a random number between 0 and 4
            switch (randomNumber) {
                case 0:
                    WebElement color1 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Pearl White\"]"));
                    color1.click();
                    break;
                case 1:
                    WebElement color2 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Golden Oakwood\"]"));
                    color2.click();
                    break;
                case 2:
                    Swipe.Right(driver, 0.9, 0.67);
                    WebElement color3 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Earth Brown\"]"));
                    color3.click();
                    break;
                case 3:
                    Swipe.Right(driver, 0.9, 0.67);
                    WebElement color4 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Natural Oakwood\"]"));
                    color4.click();
                    break;
            }
        } catch (Exception exp) {
            logpoint(exp.getMessage());
        }
    }

    public static void StudioPlus(AppiumDriver driver) {
        try {
            int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 5
            switch (randomNumber) {
                case 0:
                    WebElement color1 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Marble White\"]"));
                    color1.click();
                    break;
                case 1:
                    WebElement color2 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Earth Brown\"]"));
                    color2.click();
                    break;
            }
        } catch (Exception exp) {
            logpoint(exp.getMessage());
        }
    }

    public static void Aris(AppiumDriver driver) {
        try {
            int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 2
            switch (randomNumber) {
                case 0:
                    WebElement color1 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Marble White\"]"));
                    color1.click();
                    break;
                case 1:
                    WebElement color2 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Dark Teakwood\"]"));
                    color2.click();
                    break;
            }
        } catch (Exception exp) {
            logpoint(exp.getMessage());
        }
    }

    public static void Jaguar(AndroidDriver driver) {
        try {
            int randomNumber = (int) (Math.random() * 3); // generate a random number between 0 and 5
            switch (randomNumber) {
                case 0:
                    WebElement color1 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Marble White\"]"));
                    color1.click();
                    break;
                case 1:
                    WebElement color2 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Regent Gray\"]"));
                    color2.click();
                    break;
                case 3:
                    Swipe.Right(driver, 0.9, 0.67);
                    WebElement color3 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Matte Black\"]"));
                    color3.click();
                    break;
            }
        } catch (Exception exp) {
            logpoint(exp.getMessage());
        }
    }

    public static void Erica(AppiumDriver driver) {
        try {
            int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 2
            switch (randomNumber) {
                case 0:
                    WebElement color1 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Snow White\"]"));
                    color1.click();
                    break;
                case 1:
                    WebElement color2 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Umber Brown\"]"));
                    color2.click();
                    break;
            }
        } catch (Exception exp) {
            logpoint(exp.getMessage());
        }
    }

    public static void Saber(AppiumDriver driver) {
        //TODO : To make changes when the saber is added with the Marketing Name.
        try {
            int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 2
            switch (randomNumber) {
                case 0:
                    WebElement color1 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Snow White\"]"));
                    color1.click();
                    break;
                case 1:
                    WebElement color2 = driver
                            .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Umber Brown\"]"));
                    color2.click();
                    break;
            }
        } catch (Exception exp) {
            logpoint(exp.getMessage());
        }
    }

    //thread sleep
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logpoint(e.getMessage());
        }
    }

}

