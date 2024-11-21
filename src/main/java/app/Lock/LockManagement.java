package app.Lock;

import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static app.util.ActionsUtil.sleep;
import static app.util.AppUtil.Array;

public class LockManagement {
    public AppiumDriver driver;
    public LockManagement(AppiumDriver driver) {
        this.driver = driver;
    }

    public void addLock(){
        WebElement AddButton = null;
        try {
            AddButton = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
        } catch (Exception ignored) {
        }
        if (AddButton != null) {
            AddButton.click();
            sleep(1000);
        } else {
            ActionsUtil.Tap.withCoordinates(driver, 540, 1940);
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
                    } catch (Exception ignored) {
                    }
                    try {
                        LReset = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not add the lock\"]"));
                    } catch (Exception ignored) {
                    }
                    try {
                        FReset = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Device already paired\"]"));
                    } catch (Exception ignored) {
                    }
                    try {
                        Reach = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not reach\r\n"
                                + "the device\"]"));
                    } catch (Exception ignored) {
                    }

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
                } catch (Exception ignored) {
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

    public void lock() {
        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Locks\"]")).click();
        WebElement LO = null;  // checks Lock availability
        try {
            LO = driver.findElement(By.xpath("(//android.widget.Button/android.widget.Button)"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
                LockControl();
            }
        } else {
            System.out.println("No Lock Available");

        }
    }
    // inside the lock control
    public void LockControl() {
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
        history();  // history of lock
        sleep(5000);
        driver.navigate().back();
        AccessKeys();  //Access keys of lock
        lockSettings();   // lock settings
        driver.navigate().back();
        driver.navigate().back();

    }

    public void history() {
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

    public void lockSettings() {
        WebElement settings = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Settings\"]"));
        settings.click();  // tap on the Setting button
        Passcode();  // entering the passcode. specific to one plus, poco and redmi
        sleep(1000);
        WebElement users = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Users\"]"));
        users.click();
        sleep(1000);
        driver.navigate().back();
        PBCSettings();


    }

    public void AccessKeys() {

        WebElement AccessKeys = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Access\nkeys\"]"));
        AccessKeys.click();
        Passcode();
        KeyType();  // for clicking on all the types of keys


    }
    // set for specific devices using the Coordinates
    public void Passcode() {
        ActionsUtil.Tap.withCoordinates(driver, 540, 880);
        sleep(500);
        AppUtil.NumberPad NumberPad = new AppUtil.NumberPad();
        NumberPad.one(driver);
        NumberPad.one(driver);
        NumberPad.one(driver);
        NumberPad.two(driver);
        NumberPad.two(driver);
        NumberPad.two(driver);
        NumberPad.done(driver);
    }

    public void KeyType() {
        List<WebElement> KEYS = driver.findElements(By.className("android.widget.Button"));
        int i;
        int total = KEYS.size();
        for (i = 0; i < total; i++) {
            List<WebElement> Keys = driver.findElements(By.className("android.widget.Button"));
            WebElement key = Keys.get(i);
            boolean OTP = Objects.requireNonNull(key.getAttribute("content-desc")).endsWith("OTP"); // checks if the last string is OTP
            boolean New = Objects.requireNonNull(key.getAttribute("content-desc")).endsWith("NEW"); // checks if the last string is NEW
            System.out.println(OTP || New); // When opened 1st time the last string is NEW while in the second attempt it is OTP
            key.click();
            if (OTP || New) {
                sleep(1000);
                List<WebElement> RemoteOTPs = driver.findElements(By.className("android.view.View"));
                for (WebElement otp : RemoteOTPs) {
                    System.out.println(otp.getAttribute("content-desc"));
                }
            }
            driver.navigate().back();
            if (i < (total - 1)) { // to repeat the Access keys opening as once we go back it goes back to lock control screen
                WebElement AccessKeys = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Access\nkeys\"]"));
                AccessKeys.click();
                Passcode();
            }
        }
    }

    public void PBCSettings() {
        WebElement PBC = null; // Checks the availability on Pin, Biometrics & Cards in Lock Settings
        try {
            PBC = driver.findElement(By.xpath("//android.view.View[@content-desc=\"PINs, biometric and card settings\"]"));
        } catch (Exception ignored) {
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
                    passageMode();
                    sleep(1000);
                }
                if (i == 1) {
                    WebElement FPEnable = ts.get(i);
                    FPEnable.click();
                    fingerprint();
                }
                if (i == 2) {
                    WebElement CardEnable = ts.get(i);
                    CardEnable.click();
                    CardEnable();
                }
                if (i == 3) {
                    WebElement Pins = ts.get(i);
                    Pins.click();
                }
            }
        }
    }

    public void passageMode() {
        WebElement PMDisabled = null;
        try {
            PMDisabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Passage Mode disabled successfully\"]"));
        } catch (Exception ignored) {
        }
        if (PMDisabled == null) {
            WebElement psmText = driver.findElement(By.xpath("//android.view.View[@content-desc=\"You are enabling passage mode. Enabling this mode will allow anyone to enter the house without any authentication. Do you want to continue?\"]"));
            boolean allow = Objects.requireNonNull(psmText.getAttribute("content-desc")).endsWith("Do you want to continue?");
            if (allow) {
                WebElement Yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
                Yes.click();
            }
            WebElement psmText2 = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Please read this below. Do you still want to continue?\"]"));
            boolean allow2 = Objects.requireNonNull(psmText2.getAttribute("content-desc")).startsWith("Please read this below");
            if (allow2) {
                WebElement Yes2 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
                Yes2.click();
            }
            WebElement PMEnabled = null;
            try {
                PMEnabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Passage Mode Enabled Successfully\"]"));
            } catch (Exception ignored) {
            }
            if (PMEnabled != null) {
                System.out.println("Passage Mode Enabled Successfully");
            }

        } else {
            System.out.println("Passage Mode Disabled Successfully. Enabling it again");
            List<WebElement> TS = driver.findElements(By.className("android.widget.Switch"));
            WebElement PassageMode = TS.get(0);
            PassageMode.click();
            passageMode();
        }

    }

    public void fingerprint() {
        WebElement FPDisabled = null;
        try {
            FPDisabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Fingerprints Disabled Successfully!\"]"));
        } catch (Exception ignored) {
        }
        WebElement FPEnabled = null;
        try {
            FPEnabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Fingerprints Enabled Successfully!\"]"));
        } catch (Exception ignored) {
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

    public void CardEnable() {
        WebElement CNotAvail = null;
        WebElement CDisable = null;
        WebElement CEnabled = null;

        try {
            CNotAvail = driver.findElement(By.xpath("//android.view.View[@content-desc=\"No Cards present for this Lock.\"]"));
        } catch (Exception ignored) {
        }
        try {
            CDisable = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Cards Disabled Successfully!\"]"));
        } catch (Exception ignored) {
        }
        try {
            CEnabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Cards Enabled Successfully!\"]"));

        } catch (Exception ignored) {
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

    public void lockAdditionProcess() {
        for (int i = 1 ; i <7 ; i++)
        {
            WebElement Pin = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.EditText["+i+"]"));
            String randomNumber = String.valueOf(Array());
            Pin.sendKeys(randomNumber);
        }
        WebElement Save = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Save\"]"));
        Save.click();
        sleep(5000);
        WebElement SuccessMessage = null;
        try {
            SuccessMessage = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Added Successfully \uD83D\uDC4D\"]"));
        }
        catch(Exception ignored){}
        if (SuccessMessage != null) {
            System.out.println("Lock Added Successfully");
            sleep(1500);
        }
        sleep(3000);
    }

}
