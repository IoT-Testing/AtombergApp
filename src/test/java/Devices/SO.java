package Devices;

import Actions.NumberPad;
import Actions.Tap;
import AtombergTest.Method;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Actions.Scroll;

import io.appium.java_client.android.AndroidDriver;

public class SO {//Search Online Fan

    public static void Fan(AndroidDriver driver) {

        try {
            sleep(2500);
            WebElement AddButton = null;
            try { // checks for the + buttons availability
                AddButton = driver.findElement(By.xpath(
                        "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
            } catch (Exception exp) {
                exp.getCause();
            }
            if (AddButton != null) {
                CFO(driver);  // checks the Fan availability
            } else {
                // Fan addition process
                Add2.Fan(driver);
                CFO(driver); //checks fan availability
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            System.out.println(exp.getMessage());
            exp.printStackTrace();
        }
    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }

    public static void CFO(AndroidDriver driver) {//Check Fan Online
        WebElement Fans = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Fans\"]"));
        Fans.click();   // click on the fan tab
        sleep(3000);
        List<WebElement> FANS = driver.findElements(By.className("android.widget.Button"));
        System.out.println(FANS.size());
        List<WebElement> fans = FANS.stream().filter(dev -> dev.getDomAttribute("content-desc") != null).collect(Collectors.toList());

        if (fans.size() > 1) {
            System.out.println("Fan Available " + fans.size());
        }
        for (WebElement element : fans) {
            System.out.println(element.getDomAttribute("content-desc"));
            element.click(); // Clicks on the for and opens device control
            Method.FanControl(driver); // Controls the fan
            driver.navigate().back();            // back
        }
        String previousFan = fans.get(fans.size()-2).getDomAttribute("content-desc");
        String lastFan = fans.get(fans.size() - 1).getDomAttribute("content-desc");
        System.out.println(previousFan);
        System.out.println(lastFan);
        if (fans.size() >= 4) // only 4 devices are visible on the screen
        {
            Scroll.Up(driver);
            List<WebElement> NEWFANS = driver.findElements(By.className("android.widget.Button"));
            List<WebElement> newfans = NEWFANS.stream().filter(dev -> dev.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            if (Objects.equals(newfans.get(newfans.size() - 1).getDomAttribute("content-desc"), lastFan)) {
                System.out.println("No more devices");
            }
            System.out.println(newfans.size());
            int count = 0;
            for (WebElement fan : newfans) {

                String name = fan.getDomAttribute("content-desc");
                assert name != null;
                if (name.equals(previousFan) || name.equals(lastFan)) {
                    count ++;
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
                Method.FanControl(driver); // Controls the fan
                driver.navigate().back();            // back
            }

        }
        if (fans.isEmpty()) {
            System.out.println("No Fan Offline");
        }
    }

    public static void Lock(AndroidDriver driver) {
        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Locks\"]")).click();
        WebElement LO = null;  // checks Lock availability
        try {
            LO = driver.findElement(By.xpath("(//android.widget.Button/android.widget.Button)"));
        } catch (Exception e) {
        }

        // To Do if lock is available
        if (LO != null) {
            // i
            System.out.println("Lock Available");
            List<WebElement> Device = driver
                    .findElements(By.xpath("//android.widget.Button/android.widget.ImageView[1]"));
            System.out.println(Device.size());   // number of available locks

            for (WebElement element : Device) {
                System.out.println(element);
                element.click(); // click and open lock control
                System.out.println("Element clicked");
                LockControl(driver);
            }
        } else {
            System.out.println("No Lock Available");

        }
    }
    // inside the lock control
    public static void LockControl(AndroidDriver driver) {
        sleep(7500);
        // click on the handle(tap to unlock)
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Pull down to unlock\"]")).click();
        System.out.println("Unlocking");
        sleep(1000);
        WebElement Unlocked = null;
        WebElement NoLock = null;

        try {// checks if it is unlocked
            Unlocked = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Unlocked\"]"));
        } catch (Exception ignored) {
        }
        try {// check if it could not unlock
            NoLock = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not unlock\"]"));
        } catch (Exception ignored) {
        }
        if (Unlocked != null) {
            System.out.println("Successfully unlocked");
        } else if (NoLock != null) {
            System.out.println("Lock not available or Bluetooth off");
            driver.navigate().back();
        } else {
            System.out.println("Error");
            driver.navigate().back();
        }
        history(driver);  // history of lock
        sleep(5000);
        driver.navigate().back();
        AccessKeys(driver);  //Access keys of lock
        lockSettings(driver);   // lock settings
        driver.navigate().back();
        driver.navigate().back();

    }

    public static void history(AndroidDriver driver) {
        WebElement history = null;
        try {// check in the history button is available
            history = driver.findElement(By.xpath("//android.view.View[@content-desc=\"History\"]"));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (history != null) { // if available click on it
            history.click();
        }
    }

    public static void lockSettings(AndroidDriver driver) {
        WebElement settings = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Settings\"]"));
        settings.click();  // tap on the Setting button
        Passcode(driver);  // entering the passcode. specific to one plus, poco and redmi
        sleep(1000);
        WebElement users = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Users\"]"));
        users.click();
        sleep(1000);
        driver.navigate().back();
        PBCSettings(driver);


    }

    public static void AccessKeys(AndroidDriver driver) {

        WebElement AccessKeys = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Access\nkeys\"]"));
        AccessKeys.click();
        Passcode(driver);
        KeyType(driver);  // for clicking on all the types of keys


    }

    // set for specific devices using the Coordinates
    public static void Passcode(AndroidDriver driver) {
        Tap.withCoordinates(driver, 540, 880);
        sleep(500);
        NumberPad.one(driver);
        NumberPad.one(driver);
        NumberPad.one(driver);
        NumberPad.two(driver);
        NumberPad.two(driver);
        NumberPad.two(driver);
        NumberPad.done(driver);
    }

    public static void KeyType(AndroidDriver driver) {
        List<WebElement> KEYS = driver.findElements(By.className("android.widget.Button"));
        int i;
        int total = KEYS.size();
        for (i = 0; i < total; i++) {
            List<WebElement> Keys = driver.findElements(By.className("android.widget.Button"));
            WebElement key = Keys.get(i);
            boolean OTP = Objects.requireNonNull(key.getDomAttribute("content-desc")).endsWith("OTP"); // checks if the last string is OTP
            boolean New = Objects.requireNonNull(key.getDomAttribute("content-desc")).endsWith("NEW"); // checks if the last string is NEW
            System.out.println(OTP || New); // When opened 1st time the last string is NEW while in the second attempt it is OTP
            key.click();
            if (OTP || New) {
                sleep(1000);
                List<WebElement> RemoteOTPs = driver.findElements(By.className("android.view.View"));
                for (WebElement otp : RemoteOTPs) {
                    System.out.println(otp.getDomAttribute("content-desc"));
                }
            }

            driver.navigate().back();

            if (i < (total - 1)) { // to repeat the Access keys opening as once we go back it goes back to lock control screen
                WebElement AccessKeys = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Access\nkeys\"]"));
                AccessKeys.click();
                Passcode(driver);
            }
        }
    }

    public static void PBCSettings(AndroidDriver driver) {
        WebElement PBC = null; // Checks the availability on Pin, Biometrics & Cards in Lock Settings
        try {
            PBC = driver.findElement(By.xpath("//android.view.View[@content-desc=\"PINs, biometric and card settings\"]"));
        } catch (Exception e) {
        }
        if (PBC != null) {
            List<WebElement> TS = driver.findElements(By.className("android.widget.Switch"));
            int i;
            int total = TS.size();  //
            System.out.println(total);
            for (i = 0; 1 < total; i++) {
                List<WebElement> ts = driver.findElements(By.className("android.widget.Switch"));
                if (i == 0) {
                    WebElement PassageMode = ts.get(i);

                    PassageMode.click();
                    passageMode(driver);
                    sleep(1000);
                }
                if (i == 1) {
                    WebElement FPEnable = ts.get(i);
                    FPEnable.click();
                    fingerprint(driver);
                }
                if (i == 2) {
                    WebElement CardEnable = ts.get(i);
                    CardEnable.click();
                    CardEnable(driver);

                }
                if (i == 3) {
                    WebElement Pins = ts.get(i);
                    Pins.click();
                }
            }
        }
    }

    public static void passageMode(AndroidDriver driver) {
        WebElement PMDisabled = null;
        try {
            PMDisabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Passage Mode disabled successfully\"]"));
        } catch (Exception e) {
        }
        if (PMDisabled == null) {
            WebElement psmText = driver.findElement(By.xpath("//android.view.View[@content-desc=\"You are enabling passage mode. Enabling this mode will allow anyone to enter the house without any authentication. Do you want to continue?\"]"));
            boolean allow = Objects.requireNonNull(psmText.getDomAttribute("content-desc")).endsWith("Do you want to continue?");
            if (allow) {
                WebElement Yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
                Yes.click();
            }
            WebElement psmText2 = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Please read this below. Do you still want to continue?\"]"));
            boolean allow2 = Objects.requireNonNull(psmText2.getDomAttribute("content-desc")).startsWith("Please read this below");
            if (allow2) {
                WebElement Yes2 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
                Yes2.click();
            }
            WebElement PMEnabled = null;
            try {
                PMEnabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Passage Mode Enabled Successfully\"]"));
            } catch (Exception e) {
            }
            if (PMEnabled != null) {
                System.out.println("Passage Mode Enabled Successfully");
            }

        } else {
            System.out.println("Passage Mode Disabled Successfully. Enabling it again");
            List<WebElement> TS = driver.findElements(By.className("android.widget.Switch"));
            WebElement PassageMode = TS.get(0);
            PassageMode.click();
            passageMode(driver);
        }

    }

    public static void fingerprint(AndroidDriver driver) {
        WebElement FPDisabled = null;
        try {
            FPDisabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Fingerprints Disabled Successfully!\"]"));
        } catch (Exception e) {
        }
        WebElement FPEnabled = null;
        try {
            FPEnabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Fingerprints Enabled Successfully!\"]"));
        } catch (Exception e) {
        }
        if (FPDisabled != null) {
            System.out.println("All Fingerprints Disabled Successfully!\n Enabling them again");
            List<WebElement> TS = driver.findElements(By.className("android.widget.Switch"));
            WebElement FPEnable = TS.get(1);
            FPEnable.click();
        } else if (FPEnabled != null) {
            System.out.println("All Fingerprints Disabled Successfully");
        }
    }

    public static void CardEnable(AndroidDriver driver) {
        WebElement CNotAvail = null;
        WebElement CDisable = null;
        WebElement CEnabled = null;

        try {
            CNotAvail = driver.findElement(By.xpath("//android.view.View[@content-desc=\"No Cards present for this Lock.\"]"));
        } catch (Exception e) {
        }
        try {
            CDisable = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Cards Disabled Successfully!\"]"));
        } catch (Exception e) {
        }
        try {
            CEnabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Cards Enabled Successfully!\"]"));

        } catch (Exception e) {
        }
        if (CNotAvail != null) {
            System.out.println("No Cards present for this Lock. Please add one");
        }
        if (CDisable != null) {
            System.out.println("Cards Disabled, Enabling it ...");
            List<WebElement> TS = driver.findElements(By.className("android.widget.Switch"));
            WebElement CEnable = TS.get(2);
            CEnable.click();
        }
        if (CEnabled != null) {
            System.out.println("Cards Enabled");
        }
    }
}