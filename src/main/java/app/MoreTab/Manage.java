package app.MoreTab;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Manage {
    public AndroidDriver atomberg;

    public Manage(AndroidDriver driver) {
        this.atomberg = driver;
    }

    public void theme() {
        WebElement Theme = atomberg.findElement(By.xpath("//android.widget.ScrollView/android.widget.ImageView[5]"));
        Theme.click();
        System.out.println("Theme");

    }

    public void electricityUnitPrice() {
        WebElement unitPrice = null;
        try {
            unitPrice = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Electricity unit price\"]"));
        } catch (Exception ignored) {
        }
        if (unitPrice == null) ActionsUtil.Scroll.Up(atomberg);
        atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Electricity unit price\"]")).click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Tap on Unit Price");

        WebElement ChangeAmount = atomberg.findElement(By.xpath("//android.widget.EditText[@text=\"7.0\"]"));
        ChangeAmount.click();
        AppUtil.captureScreenshot(atomberg);
    }

    public void changeCurrency() {
        WebElement ChangeCurrency = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"INR\"]"));
        ChangeCurrency.click();
        AppUtil.captureScreenshot(atomberg);
        atomberg.navigate().back();
    }

    public void help() {
        ScreenCheck screenCheck = new ScreenCheck(atomberg);
        screenCheck.moreTab();
        List<WebElement> moreTab;
        do {
            List<WebElement> MT = atomberg.findElements(By.className("android.view.View"));
            System.out.println(MT.size());
            List<WebElement> mt = MT.stream().filter(webElement -> webElement.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println(mt.size());
            moreTab = mt.stream().filter(webElement -> Objects.equals(webElement.getDomAttribute("content-desc"), "Help")).collect(Collectors.toList());
            System.out.println(moreTab.size());
            if (moreTab.isEmpty()) {
                ActionsUtil.Scroll.Up(atomberg);
            } else if (!moreTab.get(0).isDisplayed()) // Rare case where the Logout button is in the DOM but not on the Screen
            {
                System.out.println(!moreTab.get(0).isDisplayed());
                ActionsUtil.Scroll.Up(atomberg);
            }
        } while (moreTab.isEmpty());
        atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]")).click();
    }

    public void changePassword() {
        /*
          CLICK ON THE CHANGE PASSWORD
        */
        checkLogout();
        WebElement ChangePassword = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Change password\"]"));
        ChangePassword.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Tap on Change Password");
        atomberg.navigate().back();
    }

    public void deleteAccount() {
        /*
            CLICK ON DELETE ACCOUNT
        */
        WebElement DeleteAccount = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Delete account\"]"));
        DeleteAccount.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Tap on Delete Account");
        atomberg.navigate().back();
    }

    public void developerOptions() {
        /*
            CLICK ON DEVELOPER OPTIONS
        */
        WebElement developerOptions = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Developer options\"]"));
        assert developerOptions.isDisplayed();
        developerOptions.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Tap on Developer Options");
        ActionsUtil.sleep(1000);
        atomberg.navigate().back();
    }

    public void logout() {
        /*
            CLICK ON LOGOUT
            CONFIRM LOGOUT
        */
        checkLogout();
        WebElement logout = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
        assert logout.isDisplayed();
        logout.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Tap on Logout");
        WebElement yes = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
        yes.click();
        /*driver.navigate().back();*/
    }

    public void family() {
        /*
            GO TO MORE TAB
            SCROLL TILL MANAGE FAMILY IS IN THE SCREEN
            TAP ON MANAGE FAMILY
            CHECK AVAILABLE FAMILIES
            CLICK ON FAMILY
            CLICK ON FAMILY OPTIONS
            CLICK ON LEAVE FAMILY
            CANCEL
            CLICK ON DELETE
            CANCEL
            IF SCRIPT
            DELETE FAMILY
            IF ADD
            DO ADD HOME PROCESS WITH HOME NAME -> SCRIPT
        */
        ScreenCheck screen = new ScreenCheck(atomberg);
        screen.moreTab();
        WebElement ManageFamily = null;

        while (ManageFamily == null) {
            try {
                ManageFamily = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Manage family\"]"));
            } catch (Exception ignored) {
            }
            if (ManageFamily == null) {
                ActionsUtil.Scroll.Up(atomberg);
                WebElement logout = null;
                try {
                    logout = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
                }catch (Exception ignored){}
                if(logout!=null)ActionsUtil.Scroll.Down(atomberg);
            }
        }
        ManageFamily.click();
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Tap on Manage Family");

        List<WebElement> elements = atomberg.findElements(By.className("android.widget.ImageView"));
        List<WebElement> families = elements.stream().filter(element -> element.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        System.out.println(families.size());
        int numberOfFamilies = families.size();
        for (int i = 0; i < numberOfFamilies; i++) {
            List<WebElement> familyElements = atomberg.findElements(By.className("android.widget.ImageView"));
            List<WebElement> familyElement = familyElements.stream().filter(element -> element.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            String familyName = familyElement.get(i).getDomAttribute("content-desc");
            ActionsUtil.sleep(500);
            System.out.println(familyElement.get(i).getDomAttribute("content-desc"));
            familyElement.get(i).click();
            System.out.println(Objects.equals(familyName, "Add") && familyName.endsWith("Script"));
            System.out.println(Objects.equals(familyName, "1\n" +
                    "Script"));
            if (!Objects.equals(familyName, "Add") && !Objects.requireNonNull(familyName).endsWith("Script")) {
                home();
                back();
            } else if (familyName.endsWith("Script")) {
                WebElement FamilyEdit = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[2]"));
                FamilyEdit.click();
                System.out.println("Family Edit");
                AppUtil.captureScreenshot(atomberg);
                WebElement LeaveHome = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Leave home\"]"));
                LeaveHome.click();
                System.out.println("Leave home");
                AppUtil.captureScreenshot(atomberg);

                WebElement Cancel = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
                Cancel.click();
                System.out.println("Cancel");
                AppUtil.captureScreenshot(atomberg);

                WebElement delete = null;
                try {
                    delete = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Delete home\"]"));
                } catch (Exception ignored) {
                }
                if (delete != null) {
                    delete.click();
                    WebElement yes = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
                    yes.click(); // android.widget.Button[@content-desc="Cancel"]
                    ActionsUtil.sleep(3000);
                }
                i--;
                System.out.println(numberOfFamilies);
                numberOfFamilies -= 1;
                System.out.println(numberOfFamilies);

            } else {
                addHome();
            }
        }
        atomberg.navigate().back();//Back to more tab from Manage Family
    }

    private void member() {

        WebElement RemoveMember = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Remove Member\"]"));
        RemoveMember.click();
        System.out.println("Remove Member");
        AppUtil.captureScreenshot(atomberg);

        WebElement Cancel2 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel2.click();
        WebElement MakeAdmin = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Make Admin\"]"));
        MakeAdmin.click();

        System.out.println("Make Admin");
        AppUtil.captureScreenshot(atomberg);

        WebElement Cancel3 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel3.click();
        atomberg.navigate().back();

        WebElement AddMember = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]"));
        AddMember.click();

        AppUtil.captureScreenshot(atomberg);
        ActionsUtil.sleep(3000);

        WebElement Share = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Share\"]"));
        Share.click();
        AppUtil.captureScreenshot(atomberg);

        atomberg.navigate().back();
        atomberg.navigate().back();
        atomberg.navigate().back();
    }

    private void addHome() {
        List<WebElement> accountCreation = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> buttons = accountCreation.stream().filter(element -> element.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        List<WebElement> button = buttons.stream().filter(Object -> Objects.requireNonNull(Object.getDomAttribute("content-desc")).startsWith("Create a new smart home")).collect(Collectors.toList());
        if (!button.isEmpty()) button.get(0).click();
        AppUtil.captureScreenshot(atomberg);
//        ActionsUtil.Tap.withPercentage(atomberg, 0.50, 0.75); // Narzo 0.50, 0.625 Tab 0.50, 0.75
        AppUtil.captureScreenshot(atomberg);
        WebElement HomeName = atomberg.findElement(By.xpath("//android.widget.EditText"));
        HomeName.click();
        HomeName.sendKeys("Script");
        AppUtil.captureScreenshot(atomberg);
        WebElement Create = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Create\"]"));
        Create.click();
        AppUtil.captureScreenshot(atomberg);
        ActionsUtil.sleep(5000);
    }

    private void home() {
        WebElement FamilyEdit = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[2]"));
        FamilyEdit.click();
        System.out.println("Family Edit");
        AppUtil.captureScreenshot(atomberg);

        WebElement LeaveHome = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Leave home\"]"));
        LeaveHome.click();
        System.out.println("Leave home");
        AppUtil.captureScreenshot(atomberg);

        WebElement Cancel = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel.click();
        System.out.println("Cancel");
        AppUtil.captureScreenshot(atomberg);

        WebElement delete = null;
        try {
            delete = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Delete home\"]"));
        } catch (Exception ignored) {
        }
        if (delete != null) {
            delete.click();
            Cancel = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
            Cancel.click(); // android.widget.Button[@content-desc="Cancel"]
        }
    }

    private void countMember() {
        List<WebElement> Elements = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> elements = Elements.stream().filter(element -> Objects.equals(element.getDomAttribute("clickable"), "true")).collect(Collectors.toList());
        System.out.println(elements.size());
        for (WebElement e : elements) {
            System.out.println(e.getTagName());
        }
    }

    private void back() {
        WebElement manageFamily = null;
        while (manageFamily == null) {
            try {
                manageFamily = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
            } catch (Exception ignored) {
            }
            if (manageFamily == null) atomberg.navigate().back();
        }
    }

    private void checkLogout() {
        ScreenCheck screen = new ScreenCheck(atomberg);
        screen.moreTab();
        List<WebElement> moreTab;
        do {
            List<WebElement> MT = atomberg.findElements(By.className("android.view.View"));
            System.out.println(MT.size());
            List<WebElement> mt = MT.stream().filter(webElement -> webElement.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println(mt.size());
            moreTab = mt.stream().filter(webElement -> Objects.equals(webElement.getDomAttribute("content-desc"), "Logout")).collect(Collectors.toList());
            System.out.println(moreTab.size());
            if (moreTab.isEmpty()) {
                ActionsUtil.Scroll.Up(atomberg);
            } else if (!moreTab.get(0).isDisplayed()) // Rare case where the Logout button is in the DOM but not on the Screen
            {
                System.out.println(!moreTab.get(0).isDisplayed());
                ActionsUtil.Scroll.Up(atomberg);
            }
        } while (moreTab.isEmpty());
    }
}

