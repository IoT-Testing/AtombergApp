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
    public AppiumDriver atomberg;
    public LockManagement(AppiumDriver driver) {
        this.atomberg = driver;
    }

    public void addLock(){
        WebElement AddButton = null;
        try {
            AddButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
        } catch (Exception ignored) {
        }
        if (AddButton != null) {
            AddButton.click();
            sleep(1000);
        } else {
            ActionsUtil.Tap.withCoordinates(atomberg, 540, 1940);
            sleep(1000);
        }
        System.out.println("Searching for Available devices");
        for (int c = 0; c < 10; c++) {
            sleep(15000);
            WebElement element = null;
            String xpathExpression = "//android.view.View[@content-desc=\"Atomberg Smart Lock\"]";
            // Search Fan Only
            try {
                element = atomberg.findElement(By.xpath(xpathExpression));
            } catch (NoSuchElementException ignored) {
            }

            if (element != null) // if device is available
            {
                System.out.println("Lock Available");
                List<WebElement> Connects = atomberg.findElements(By.xpath("(//android.view.View[@content-desc=\"Connect\"])"));

                for (int i = 1; i <= Connects.size(); i++) {

                    WebElement Connect = atomberg.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[" + i + "]"));
                    System.out.println("Connect button at : " + i);
                    Connect.click();
                    WebElement FAdd = null;  //(//android.view.View[@content-desc="Connect"])[2]
                    WebElement LReset = null;//(//android.view.View[@content-desc="Connect"])[2]
                    WebElement FReset = null;
                    WebElement Reach = null;
                    try {
                        FAdd = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Identify your device\"]"));
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

                    if (FAdd != null || LReset != null || FReset != null || Reach != null) {
                        if (FAdd != null) {
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
                    } else {
                        System.out.println("Breaking the loop");
                        break;
                    }
                }
            } else {
                WebElement DD = null;//discovered devices
                try {
                    DD = atomberg.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[2]"));
                } catch (Exception ignored) {
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

    public void lockAdditionProcess() {
        for (int i = 1 ; i <7 ; i++)
        {
            WebElement Pin = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.EditText["+i+"]"));
            String randomNumber = String.valueOf(Array());
            Pin.sendKeys(randomNumber);
        }
        WebElement Save = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Save\"]"));
        Save.click();
        sleep(5000);
        WebElement SuccessMessage = null;
        try {
            SuccessMessage = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Added Successfully \uD83D\uDC4D\"]"));
        }
        catch(Exception ignored){}
        if (SuccessMessage != null) {
            System.out.println("Lock Added Successfully");
            sleep(1500);
        }
        sleep(3000);
    }

    public void checkLock() {
        atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Locks\"]")).click();
        WebElement LO = null;  // checks Lock availability
        try {
            LO = atomberg.findElement(By.xpath("(//android.widget.Button/android.widget.Button)"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // To Do if lock is available
        if (LO != null) {
            // i
            System.out.println("Lock Available");
            List<WebElement> Device = atomberg
                    .findElements(By.xpath("//android.widget.Button/android.widget.ImageView[1]"));
            System.out.println(Device.size());   // number of available locks

            for (WebElement element : Device) {
                element.click(); // click and open lock control
                LockControl();
            }
        } else {
            System.out.println("No Lock Available");

        }
    }
    // inside the lock control
    public void LockControl() {
        ActionsUtil.sleep(7500);
        // click on the handle(tap to unlock)
        atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Pull down to unlock\"]")).click();
        System.out.println("Unlocking");
        ActionsUtil.sleep(1000);
        WebElement Unlocked = null;
        WebElement NoLock = null;

        try {// checks if it is unlocked
            Unlocked = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Unlocked\"]"));
        } catch (Exception ignored) {
        }
        try {// check if it could not unlock
            NoLock = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Could not unlock\"]"));
        } catch (Exception ignored) {
        }
        if (Unlocked != null) {
            System.out.println("Successfully unlocked");
        } else if (NoLock != null) {
            System.out.println("Lock not available or Bluetooth off");
            atomberg.navigate().back();
        } else {
            System.out.println("Error");
            atomberg.navigate().back();
        }
        ActionsUtil.sleep(5000);
        history();  // history of lock
        ActionsUtil.sleep(5000);
        atomberg.navigate().back();
        AccessKeys();  //Access keys of lock
        lockSettings();   // lock settings
        atomberg.navigate().back();
        atomberg.navigate().back();
    }

    private void history() {
        WebElement history = null;
        try {// check in the history button is available
            history = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"History\"]"));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (history != null) { // if available click on it
            history.click();
        }
    }

    private void lockSettings() {
        WebElement settings = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Settings\"]"));
        settings.click();  // tap on the Setting button
//        Passcode();  // entering the passcode. specific to one plus, poco and redmi
        ActionsUtil.sleep(1000);
        WebElement users = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Users\"]"));
        users.click();
        ActionsUtil.sleep(1000);
        atomberg.navigate().back();
        preferences();
    }

    private void AccessKeys() {

        WebElement AccessKeys = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Access\nkeys\"]"));
        AccessKeys.click();
//        Passcode();
        KeyType();  // for clicking on all the types of keys


    }

    // set for specific devices using the Coordinates
    //TODO : Prerequisites > Please disable the Lock of the mobile phone, so that, when lock settings are accessed, there won't be any authentication....
    protected void Passcode() {
        ActionsUtil.Tap.withCoordinates(atomberg, 540, 880);
        ActionsUtil.sleep(500);
        AppUtil.NumberPad NumberPad = new AppUtil.NumberPad();
        NumberPad.one(atomberg);
        NumberPad.one(atomberg);
        NumberPad.one(atomberg);
        NumberPad.two(atomberg);
        NumberPad.two(atomberg);
        NumberPad.two(atomberg);
        NumberPad.done(atomberg);
    }

    private void KeyType() {
        List<WebElement> KEYS = atomberg.findElements(By.className("android.widget.Button"));
        int i;
        int total = KEYS.size();
        System.out.println(total);
        for (i = 0; i < total; i++) {
            List<WebElement> Keys = atomberg.findElements(By.className("android.widget.Button"));
            WebElement key = Keys.get(i);
            boolean OTP = Objects.requireNonNull(key.getDomAttribute("content-desc")).startsWith("Remote OTP"); // checks if the last string is OTP
            boolean Pin = Objects.requireNonNull(key.getDomAttribute("content-desc")).startsWith("Remote Timed Pin"); // checks if the last string is NEW
            key.click();
            if (OTP) {
                ActionsUtil.sleep(1000);
                List<WebElement> elements = atomberg.findElements(By.className("android.view.View"));
                List<WebElement> RemoteOTPs = elements.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
                RemoteOTPs.remove(RemoteOTPs.size()-1);
                for (WebElement otp : RemoteOTPs) {
                    System.out.println(otp.getDomAttribute("content-desc"));
                }
            }
            if(Pin){
                ActionsUtil.sleep(1000);
                List<WebElement> elements = atomberg.findElements(By.className("android.view.View"));
                List<WebElement> dialogueBox = elements.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
                System.out.println(dialogueBox.size());
                for (WebElement e : dialogueBox){
                    System.out.println(e.getDomAttribute("content-desc"));
                    if (Objects.requireNonNull(e.getDomAttribute("content-desc")).startsWith("End")){
                        e.click();
                        periodicTimedPin();
                        atomberg.navigate().back();
                    }
                }
            }
            atomberg.navigate().back();
            if (i < (total - 1)) { // to repeat the Access keys opening as once we go back it goes back to lock control screen
                WebElement AccessKeys = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Access\nkeys\"]"));
                AccessKeys.click();
//                Passcode();
            }
        }
    }

    private void preferences() {
        WebElement PBC = null; // Checks the availability on Pin, Biometrics & Cards in Lock Settings
        try {
            PBC = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Preferences\"]"));
        } catch (Exception ignored) {
        }
        if (PBC != null) {
            List<WebElement> TS = atomberg.findElements(By.className("android.widget.Switch"));
            int i;
            int total = TS.size();  //
            System.out.println(total);
            for (i = 0; 1 < total; i++) {
                List<WebElement> ts = atomberg.findElements(By.className("android.widget.Switch"));
                if(i == 0){
                    WebElement silentMode = ts.get(i);
                    silentMode.click();
                    silentMode();
                    ActionsUtil.sleep(1000);
                }
                if (i == 1) {
                    WebElement PassageMode = ts.get(i);

                    PassageMode.click();
                    passageMode();
                    ActionsUtil.sleep(1000);
                }
                if (i == 2) {
                    WebElement FPEnable = ts.get(i);
                    FPEnable.click();
                    fingerprint();
                }
                if (i == 3) {
                    WebElement CardEnable = ts.get(i);
                    CardEnable.click();
                    CardEnable();
                }
                if (i == 4) {
                    WebElement Pins = ts.get(i);
                    Pins.click();
                }
            }
        }
    }

    private void passageMode() {
        WebElement passageModeDisabled = null;
        try {
            passageModeDisabled = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Passage Mode disabled successfully\"]"));
        } catch (Exception ignored) {
        }
        if (passageModeDisabled == null) {
            WebElement psmText = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"You are enabling passage mode. Enabling this mode will allow anyone to enter the house without any authentication. Do you want to continue?\"]"));
            boolean allow = Objects.requireNonNull(psmText.getDomAttribute("content-desc")).endsWith("Do you want to continue?");
            if (allow) {
                WebElement Yes = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
                Yes.click();
            }
            WebElement psmText2 = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Please read this below. Do you still want to continue?\"]"));
            boolean allow2 = Objects.requireNonNull(psmText2.getDomAttribute("content-desc")).startsWith("Please read this below");
            if (allow2) {
                WebElement Yes2 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
                Yes2.click();
            }
            WebElement PMEnabled = null;
            try {
                PMEnabled = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Passage Mode Enabled Successfully\"]"));
            } catch (Exception ignored) {
            }
            if (PMEnabled != null) {
                System.out.println("Passage Mode Enabled Successfully");
            }

        } else {
            System.out.println("Passage Mode Disabled Successfully. Enabling it again");
            List<WebElement> TS = atomberg.findElements(By.className("android.widget.Switch"));
            WebElement PassageMode = TS.get(0);
            PassageMode.click();
            passageMode();
        }
    }

    private void fingerprint() {
        WebElement FPDisabled = null;
        try {
            FPDisabled = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"All Fingerprints Disabled Successfully!\"]"));
        } catch (Exception ignored) {
        }
        WebElement FPEnabled = null;
        try {
            FPEnabled = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"All Fingerprints Enabled Successfully!\"]"));
        } catch (Exception ignored) {
        }
        if (FPDisabled != null) {
            System.out.println("All Fingerprints Disabled Successfully!\n Enabling them again");
            List<WebElement> TS = atomberg.findElements(By.className("android.widget.Switch"));
            WebElement FPEnable = TS.get(1);
            FPEnable.click();
        } else if (FPEnabled != null) {
            System.out.println("All Fingerprints Disabled Successfully");
        }
    }

    private void CardEnable() {
        WebElement CNotAvail = null;
        WebElement CDisable = null;
        WebElement CEnabled = null;

        try {
            CNotAvail = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"No Cards present for this Lock.\"]"));
        } catch (Exception ignored) {
        }
        try {
            CDisable = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"All Cards Disabled Successfully!\"]"));
        } catch (Exception ignored) {
        }
        try {
            CEnabled = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"All Cards Enabled Successfully!\"]"));

        } catch (Exception ignored) {
        }
        if (CNotAvail != null) {
            System.out.println("No Cards present for this Lock. Please add one");
        }
        if (CDisable != null) {
            System.out.println("Cards Disabled, Enabling it ...");
            List<WebElement> TS = atomberg.findElements(By.className("android.widget.Switch"));
            WebElement CEnable = TS.get(2);
            CEnable.click();
        }
        if (CEnabled != null) {
            System.out.println("Cards Enabled");
        }
    }

    private void periodicTimedPin(){
        List<WebElement> scrollableElements = atomberg.findElements(By.className("android.widget.SeekBar"));
        List<WebElement> elements = scrollableElements.stream().filter(webElement -> webElement.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        for (WebElement e: elements){
            e.click();
        }
    }

    private void silentMode() {
        WebElement enableSilentMode = null;
        try {
            enableSilentMode = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Do you want to enable silent mode?\"]"));
        } catch (Exception ignored) {}
        WebElement disableSilentMode = null;
        try {
            disableSilentMode = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Do you want to disable silent mode?\"]"));
        } catch (Exception ignored) {}
        if (enableSilentMode != null) {
            atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]")).click();
        }
        else if (disableSilentMode != null){
            atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]")).click();
        }
        ActionsUtil.SSleep(2);
        atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Silent Mode\"]/android.view.View")).click();
        WebElement silentModeInfo = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"When silent mode is enabled, unlock tune and keypad beeps are suppressed\"]"));
        assert silentModeInfo.isDisplayed();
    }


}
