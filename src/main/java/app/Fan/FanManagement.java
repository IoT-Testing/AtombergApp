package app.Fan;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static app.util.ActionsUtil.sleep;
import static app.util.AppUtil.SearchWiFi;
// 50:78:7d:19:16:88
public class FanManagement {
    public AndroidDriver atomberg;

    public FanManagement(AndroidDriver driver){
        this.atomberg = driver;
    }

    public void addFan() {
        WebElement AddButton = null;
        try {
            AddButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
        } catch (Exception ignored) {}
        if (AddButton != null) {
            AddButton.click();
            sleep(1000);
        } else {
            ActionsUtil.Tap.withCoordinates(atomberg, 540, 1850);
            sleep(1000);
        }
        System.out.println("Searching for Available devices");
        for (int c = 0; c < 10; c++) {
            sleep(15000);
            WebElement element = null;
            String xpathExpression = "//android.view.View[@content-desc=\"Atomberg Smart Fan\"]";
            // Search Fan Only
            try {
                element = atomberg.findElement(By.xpath(xpathExpression));
            } catch (NoSuchElementException ignored) {
            }

            if (element != null) // if device is available
            {
                System.out.println("Fans Available");
                List<WebElement> Connects = atomberg.findElements(By.xpath("(//android.view.View[@content-desc=\"Connect\"])"));
                for (int i = 1; i <= Connects.size(); i++) {

                    WebElement Connect = atomberg.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[" + i + "]"));
                    System.out.println("Connect button at : " + i);
                    Connect.click();
                    WebElement LAdd = null;  //(//android.view.View[@content-desc="Connect"])[2]
                    WebElement LReset = null;//(//android.view.View[@content-desc="Connect"])[2]
                    WebElement FReset = null;
                    WebElement Reach = null;
                    try {
                        LAdd = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Connecting to the Lock...\r\n"
                                + "Please don't press back button\"]"));
                    } catch (Exception ignored) {

                    }
                    try {
                        LReset = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Could not add the lock\"]"));
                    } catch (Exception ignored) {
                    }
                    try {
                        FReset = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Device already paired\"]"));
                    } catch (Exception ignored) {
                    }
                    try {
                        Reach = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Could not reach\r\n"
                                + "the device\"]"));
                    } catch (Exception ignored) {
                    }

                    if (LAdd != null || LReset != null || FReset != null || Reach != null) {
                        if (LAdd != null) {
                            atomberg.navigate().back();
                            System.out.println("Back");
                        } else if (Reach != null) {
                            System.out.println("Out of Reach");
                            atomberg.navigate().back();
                            atomberg.navigate().back();
                            System.out.println("Back");
                        } else {
                            atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                            System.out.println("Cancel button clicked");
                            sleep(1000);
                        }
                    }
                    else {
                        System.out.println("Breaking the loop");
                        break;
                    }
                }

            } else {
                WebElement DD = null;//discovered devices
                try {
                    DD = atomberg.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[2]"));
                } catch (Exception e) {System.out.println(e.getMessage());
                }
                if (DD != null) {
                    atomberg.findElement(By.xpath("//android.widget.Button")).click();
                } else {
                    WebElement tryAgain = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Try Again\"]"));
                    tryAgain.click();
                    System.out.println("Trying Again...");
                }
                continue;
            }
            System.out.println("Breaking the loop");
            break;
        }
    }

    public void additionProcess() {
        Select.Fan(atomberg);
        WebElement Next = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Next\"]")); // Add 2nd
        // Device
        System.out.println("Next");
        Next.click();
        AppUtil.captureScreenshot(atomberg);

        // Select Room
        AppUtil.additionProcess(atomberg);
    }

    public void fanControl() {

        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebElement Speed1 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"1\"]"));
        Speed1.click();
        System.out.println("Speed1");

        WebElement Speed2 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"2\"]"));
        Speed2.click();
        System.out.println("Speed2");

        WebElement Speed3 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"3\"]"));
        Speed3.click();
        System.out.println("Speed3");

        WebElement Speed4 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"4\"]"));
        Speed4.click();
        System.out.println("Speed4");

        WebElement Speed5 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"5\"]"));
        Speed5.click();
        System.out.println("Speed5");

        WebElement Boost = atomberg.findElement(By.xpath(
                "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button[5]"));
        Boost.click();
        System.out.println("Boost");

        WebElement power = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button[4]"));
        power.click();
        System.out.println("Power");
    }

    public void checkFanOnline() {//Check Fan Online
        WebElement Fans = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Fans\"]"));
        Fans.click();   // click on the fan tab
        sleep(3000);
        WebElement buyNow = null;
        try {
            buyNow = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Buy Now!\"]"));
        }catch (Exception ignored){}
        if(buyNow==null) {
            List<WebElement> FANS = atomberg.findElements(By.className("android.widget.Button"));
            System.out.println(FANS.size());
            List<WebElement> fans = FANS.stream().filter(dev -> dev.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            if (fans.size() > 1) {
                System.out.println("Fan Available " + fans.size());
            }
            for (WebElement element : fans) {
                System.out.println(element.getDomAttribute("content-desc"));
                element.click();// Clicks on the for and opens device control
                fanControl();
                atomberg.navigate().back();            // back
            }
            if (fans.size() >= 4) // only 4 devices are visible on the screen
            {
                String previousFan = fans.get(fans.size() - 2).getDomAttribute("content-desc");
                String lastFan = fans.get(fans.size() - 1).getDomAttribute("content-desc");
                ActionsUtil.Scroll.Up(atomberg);
                List<WebElement> NEWFANS = atomberg.findElements(By.className("android.widget.Button"));
                List<WebElement> newfans = NEWFANS.stream().filter(dev -> dev.getDomAttribute("content-desc") != null).collect(Collectors.toList());
                if (Objects.equals(newfans.get(newfans.size() - 1).getDomAttribute("content-desc"), lastFan)) { // to check if there are more than 4 fans in family
                    System.out.println("No more devices");
                }
                System.out.println(newfans.size());
                int count = 0;
                for (WebElement fan : newfans) {

                    String name = fan.getDomAttribute("content-desc");
                    assert name != null;
                    if (name.equals(previousFan) || name.equals(lastFan)) {
                        count++;
                    }
                }
                if (count > 0) {
                    newfans.subList(0, count).clear();
                    System.out.println("Fans Removed with new " + newfans.size() + " available");
                }
                System.out.println(newfans.size());
                for (int i = 0; i < newfans.size(); i++) {
                    String name = newfans.get(i).getDomAttribute("content-desc");
                    System.out.println(i + name);
                    newfans.get(i).click(); // Clicks on the for and opens device control
                    fanControl(); // Controls the fan
                    atomberg.navigate().back();            // back
                }
            }
            if (fans.isEmpty()) {
                System.out.println("No Fan Online");
            }
        }
        ActionsUtil.sleep(1000);
    }

    public static class Select {
        public static void Fan(AndroidDriver atomberg) {
            WebElement ModelSelect = null;
            try {
                ModelSelect = atomberg
                        .findElement(By.xpath("//android.view.View[@content-desc=\"Pick the fan model you're having\"]"));
                System.out.println("Six LED fan");
            } catch (Exception ignored) {}
            if (ModelSelect != null) {
                SixLED(atomberg);
            } else {
                FanModels fanModels = new FanModels(atomberg);
                WebElement Others = null;
                try {
                    Others = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Select your device color\"]"));
                    System.out.println("Select your fan color");
                } catch (Exception ignored) {}
                if (Others != null) {
                    WebElement Aris = null;
                    WebElement Jaguar = null;
                    try {
                        Aris = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Dark Teakwood\"]"));
                    } catch (Exception ignored) {}
                    try {
                        Jaguar = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Regent Gray\"]"));
                    } catch (Exception ignored) {}
                    if (Aris != null) {
                        fanModels.Aris();
                    } else if (Jaguar != null) {
                        fanModels.Jaguar();
                    } else {
                        fanModels.Erica();
                    }
                    AppUtil.captureScreenshot(atomberg);
                }
                else
                {
                    SixLED(atomberg);
                }
            }
        }

        public static void SixLED(AndroidDriver atomberg) {
            int randomNumber = (int) (Math.random() * 3); // generate a random number between 0 and 5
            try {
                switch (randomNumber) {
                    case 0:
                        WebElement Renesa = atomberg
                                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa\"]")); // Add 1st
                        // Device
                        Renesa.click();
                        System.out.println("Renesa Selected");
                        AppUtil.captureScreenshot(atomberg);
                        break;
                    case 1:
                        WebElement StudioPlus = atomberg
                                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio+\"]"));
                        StudioPlus.click();
                        System.out.println("Studio+ Selected");
                        AppUtil.captureScreenshot(atomberg);
                        break;
                    case 2:
                        WebElement RenesaPlus = atomberg
                                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa+\"]"));
                        RenesaPlus.click();
                        System.out.println("Renesa+ Selected");
                        AppUtil.captureScreenshot(atomberg);
                        break;

                }
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
            }
            atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]")).click();
            FanModels fanModels = new FanModels(atomberg);
            fanModels.SixLEDColorSelect();
        }
    }

    public void checkFan() {
        ScreenCheck screen = new ScreenCheck(atomberg);
        screen.homeScreen();
        WebElement emptyFamily = null;
        try {
            emptyFamily = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]"));
        } catch (Exception ignored) {}
        if (emptyFamily==null)
        {
            checkFanOnline();
        }
    }

    private void randomFanCommands(int iteration){
        atomberg.manage().timeouts().implicitlyWait(Duration.ZERO);
       for(int i =0; i<iteration; i++) {
           Random random = new Random();
           int command = random.nextInt(7);
           switch (command) {
               case 0:
                   WebElement Speed1 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"1\"]"));
                   Speed1.click();
                   System.out.println("Speed1");
                   break;
               case 1:
                   WebElement Speed2 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"2\"]"));
                   Speed2.click();
                   System.out.println("Speed2");
                   break;
               case 2:
                   WebElement Speed3 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"3\"]"));
                   Speed3.click();
                   System.out.println("Speed3");
                   break;
               case 3:
                   WebElement Speed4 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"4\"]"));
                   Speed4.click();
                   System.out.println("Speed4");
                   break;
               case 4:
                   WebElement Speed5 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"5\"]"));
                   Speed5.click();
                   System.out.println("Speed5");
                   break;
               case 5:
                   WebElement Boost = atomberg.findElement(By.xpath(
                           "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button[5]"));
                   Boost.click();
                   System.out.println("Boost");
                   break;
               case 6:
                   WebElement power = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button[4]"));
                   power.click();
                   System.out.println("Power");
                   break;
           }
           atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
       }
       atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public void repeatCommands(int iteration) {
        WebElement Speed1 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"1\"]"));
        WebElement power = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button[4]"));
        WebElement Boost = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button[5]"));
        WebElement Speed2 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"2\"]"));
        WebElement Speed3 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"3\"]"));
        WebElement Speed4 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"4\"]"));
        WebElement Speed5 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"5\"]"));
        atomberg.manage().timeouts().implicitlyWait(Duration.ZERO);
        for (int i= 0; i< iteration; i++){
            Speed1.click();
            Speed2.click();
            Speed3.click();
            Speed4.click();
            Speed5.click();
            Boost.click();
            power.click();
        }
    }

    public void LED(){
        ActionsUtil.Tap.withCoordinates(atomberg, 540, 1800);
        ActionsUtil.SSleep(10);
    }
}
