package Devices;

import Actions.Tap;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;


import io.appium.java_client.AppiumDriver;

public class Add {
    public static AppiumDriver driver;

    public static void Fan(AppiumDriver driver) {
        WebElement AddButton = null;
        try {
            AddButton = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
        } catch (Exception exp) {
        }
        if (AddButton != null) {
            AddButton.click();
            sleep(1000);
        } else {
            Tap.withCoordinates(driver, 540, 1940);
            sleep(1000);
        }
        System.out.println("Searching for Available devices");
        for (int c = 0; c < 10; c++) {
            sleep(15000);
            WebElement element = null;
            String xpathExpression = "//android.view.View[@content-desc=\"Atomberg Smart Fan\"]";
            // Search Fan Only
            try {
                element = driver.findElement(By.xpath(xpathExpression));
            } catch (NoSuchElementException ignored) {
            }

            if (element != null) // if device is available
            {
                System.out.println("Fans Available");
                List<WebElement> Connects = driver.findElements(By.xpath("(//android.view.View[@content-desc=\"Connect\"])"));
                for (int i = 1; i <= Connects.size(); i++) {
                    WebElement Connect = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[" + i + "]"));
                    System.out.println("Connect button at : " + i);
                    Connect.click();
                    WebElement LAdd = null;  //(//android.view.View[@content-desc="Connect"])[2]
                    WebElement LReset = null;//(//android.view.View[@content-desc="Connect"])[2]
                    WebElement FReset = null;
                    WebElement Reach = null;
                    try {
                        LAdd = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Connecting to the Lock...\r\n"
                                + "Please don't press back button\"]"));
                    } catch (Exception e) {
                    }
                    try {
                        LReset = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not add the lock\"]"));
                    } catch (Exception e) {
                    }
                    try {
                        FReset = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Device already paired\"]"));
                    } catch (Exception e) {
                    }
                    try {
                        Reach = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not reach\r\n"
                                + "the device\"]"));
                    } catch (Exception e) {
                    }
                    if (LAdd != null || LReset != null || FReset != null || Reach != null) {
                        if (LAdd != null) {
                            driver.navigate().back();
                            System.out.println("Back");
                        } else if (Reach != null) {
                            System.out.println("Out of Reach");
                            driver.navigate().back();
                            driver.navigate().back();
                            System.out.println("Back");
                        } else {
                            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                            System.out.println("Cancel button clicked");
                            sleep(1000);
                        }
                    } else {
                        System.out.println("Breaking the loop");
                        break;
                    }
                }

            } else {
                WebElement DD = null;//discovered devices
                try {
                    DD = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[2]"));
                } catch (Exception e) {
                }
                if (DD != null) {
                    driver.findElement(By.xpath("//android.widget.Button")).click();
                } else {
                    WebElement tryAgain = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Try Again\"]"));
                    tryAgain.click();
                    System.out.println("Trying Again...");
                }
                continue;
            }
            System.out.println("Breaking the loop");
            break;
        }
    }

    public static void Lock(AppiumDriver driver) {
        WebElement AddButton = null;
        try {
            AddButton = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
        } catch (Exception exp) {
        }
        if (AddButton != null) {
            AddButton.click();
            sleep(1000);
        } else {
            Tap.withCoordinates(driver, 540, 1940);
            sleep(1000);
        }
        System.out.println("Searching for Available devices");
        for (int c = 0; c < 10; c++) {
            sleep(15000);
            WebElement element = null;
            String xpathExpression = "//android.view.View[@content-desc=\"Atomberg Smart Lock\"]";
            // Search Fan Only
            try {
                element = driver.findElement(By.xpath(xpathExpression));
            } catch (NoSuchElementException ignored) {
            }

            if (element != null) // if device is available
            {
                System.out.println("Lock Available");
                List<WebElement> Connects = driver.findElements(By.xpath("(//android.view.View[@content-desc=\"Connect\"])"));
                for (int i = 1; i <= Connects.size(); i++) {
                    WebElement Connect = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[" + i + "]"));
                    System.out.println("Connect button at : " + i);
                    Connect.click();
                    WebElement FAdd = null;  //(//android.view.View[@content-desc="Connect"])[2]
                    WebElement LReset = null;//(//android.view.View[@content-desc="Connect"])[2]
                    WebElement FReset = null;
                    WebElement Reach = null;
                    try {
                        FAdd = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Identify your device\"]"));
                    } catch (Exception e) {
                    }
                    try {
                        LReset = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not add the lock\"]"));
                    } catch (Exception e) {
                    }
                    try {
                        FReset = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Device already paired\"]"));
                    } catch (Exception e) {
                    }
                    try {
                        Reach = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not reach\r\n"
                                + "the device\"]"));
                    } catch (Exception e) {}
                    if (FAdd != null || LReset != null || FReset != null || Reach != null) {
                        if (FAdd != null) {
                            driver.navigate().back();
                            System.out.println("Back");
                        } else if (Reach != null) {
                            System.out.println("Out of Reach");
                            driver.navigate().back();
                            driver.navigate().back();
                            System.out.println("Back");
                        } else {
                            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                            System.out.println("Cancel button clicked");
                            sleep(1000);
                        }
                    } else {
                        System.out.println("Breaking the loop");
                        break;
                    }

                }
            } else {
                WebElement DD = null;//discovered devices
                try {
                    DD = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[2]"));
                } catch (Exception e) {
                }
                if (DD != null) {
                    driver.findElement(By.xpath("//android.widget.Button")).click();
                } else {
                    WebElement tryAgain = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Try Again\"]"));
                    tryAgain.click();
                    System.out.println("Trying Again...");
                }
                continue;
            }
            System.out.println("Breaking the loop");
            break;
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}