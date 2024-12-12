package app.MoreTab;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Manage {
    public AppiumDriver driver;

    public Manage(AppiumDriver driver) {
        this.driver = driver;
    }

    public void theme() {
        WebElement Theme = driver.findElement(By.xpath("//android.widget.ScrollView/android.widget.ImageView[5]"));
        Theme.click();
        System.out.println("Theme");

    }

    public void electricityUnitPrice() {
        WebElement unitPrice = null;
        try {
            unitPrice = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Electricity unit price\"]"));
        } catch (Exception ignored) {
        }
        if (unitPrice == null) ActionsUtil.Scroll.Up(driver);
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Electricity unit price\"]")).click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Unit Price");

        WebElement ChangeAmount = driver.findElement(By.xpath("//android.widget.EditText[@text=\"7.0\"]"));
        ChangeAmount.click();
        AppUtil.captureScreenshot(driver);
    }

    public void changeCurrency() {
        WebElement ChangeCurrency = driver.findElement(By.xpath("//android.view.View[@content-desc=\"INR\"]"));
        ChangeCurrency.click();
        AppUtil.captureScreenshot(driver);
        driver.navigate().back();
    }

    public void help() {
        ScreenCheck screenCheck = new ScreenCheck(driver);
        screenCheck.moreTab();
        List<WebElement> moreTab;
        do {
            List<WebElement> MT = driver.findElements(By.className("android.view.View"));
            System.out.println(MT.size());
            List<WebElement> mt = MT.stream().filter(webElement -> webElement.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println(mt.size());
            moreTab = mt.stream().filter(webElement -> Objects.equals(webElement.getDomAttribute("content-desc"), "Help")).collect(Collectors.toList());
            System.out.println(moreTab.size());
            if (moreTab.isEmpty()) {
                ActionsUtil.Scroll.Up(driver);
            } else if (!moreTab.get(0).isDisplayed()) // Rare case where the Logout button is in the DOM but not on the Screen
            {
                System.out.println(!moreTab.get(0).isDisplayed());
                ActionsUtil.Scroll.Up(driver);
            }
        } while (moreTab.isEmpty());
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]")).click();
    }

    public void changePassword() {
        /*
          CLICK ON THE CHANGE PASSWORD
        */
        checkLogout();
        WebElement ChangePassword = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Change password\"]"));
        ChangePassword.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Change Password");
        driver.navigate().back();
    }

    public void deleteAccount() {
        /*
            CLICK ON DELETE ACCOUNT
        */
        WebElement DeleteAccount = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Delete account\"]"));
        DeleteAccount.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Delete Account");
        driver.navigate().back();
    }

    public void developerOptions() {
        /*
            CLICK ON DEVELOPER OPTIONS
        */
        WebElement developerOptionss = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Developer options\"]"));
        assert developerOptionss.isDisplayed();
        developerOptionss.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Developer Options");
        ActionsUtil.sleep(1000);
        driver.navigate().back();
    }

    public void logout() {
        /*
            CLICK ON LOGOUT
            CONFIRM LOGOUT
        */
        checkLogout();
        WebElement logout = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
        assert logout.isDisplayed();
        logout.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Logout");
        WebElement yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
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
        ScreenCheck screen = new ScreenCheck(driver);
        screen.moreTab();
        WebElement ManageFamily = null;
        WebElement logout = null;
        while (ManageFamily == null) {
            try {
                ManageFamily = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Manage family\"]"));
            } catch (Exception ignored) {
            }
            try {
                logout = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
            }catch (Exception ignored){}
            if (ManageFamily == null) ActionsUtil.Scroll.Up(driver);
            else if (ManageFamily == null && logout != null) ActionsUtil.Scroll.Down(driver);
        }
        ManageFamily.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Tap on Manage Family");

        List<WebElement> elements = driver.findElements(By.className("android.widget.ImageView"));
        List<WebElement> families = elements.stream().filter(element -> element.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        System.out.println(families.size());
        int numberOfFamilies = families.size();
        for (int i = 0; i < numberOfFamilies; i++) {
            List<WebElement> familyElements = driver.findElements(By.className("android.widget.ImageView"));
            List<WebElement> familyElement = familyElements.stream().filter(element -> element.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            String familyName = familyElement.get(i).getDomAttribute("content-desc");
            ActionsUtil.sleep(500);
            System.out.println(familyElement.get(i).getDomAttribute("content-desc"));
            familyElement.get(i).click();
            System.out.println(Objects.equals(familyName, "Add") && Objects.equals(familyName, "1\n" +
                    "Script"));
            System.out.println(Objects.equals(familyName, "1\n" +
                    "Script"));
            if (!Objects.equals(familyName, "Add") && !Objects.equals(familyName, "1\n" +
                    "Script")) {
                home();
                back();
            } else if (Objects.equals(familyName, "1\n" +
                    "Script")) {
                WebElement FamilyEdit = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[2]"));
                FamilyEdit.click();
                System.out.println("Family Edit");
                AppUtil.captureScreenshot(driver);
                WebElement LeaveHome = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Leave home\"]"));
                LeaveHome.click();
                System.out.println("Leave home");
                AppUtil.captureScreenshot(driver);

                WebElement Cancel = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
                Cancel.click();
                System.out.println("Cancel");
                AppUtil.captureScreenshot(driver);

                WebElement delete = null;
                try {
                    delete = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Delete home\"]"));
                } catch (Exception ignored) {
                }
                if (delete != null) {
                    delete.click();
                    WebElement yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
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
        driver.navigate().back();//Back to more tab from Manage Family
    }

    private void member() {

        WebElement RemoveMember = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Remove Member\"]"));
        RemoveMember.click();
        System.out.println("Remove Member");
        AppUtil.captureScreenshot(driver);

        WebElement Cancel2 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel2.click();
        WebElement MakeAdmin = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Make Admin\"]"));
        MakeAdmin.click();

        System.out.println("Make Admin");
        AppUtil.captureScreenshot(driver);

        WebElement Cancel3 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel3.click();
        driver.navigate().back();

        WebElement AddMember = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]"));
        AddMember.click();

        AppUtil.captureScreenshot(driver);
        ActionsUtil.sleep(3000);

        WebElement Share = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Share\"]"));
        Share.click();
        AppUtil.captureScreenshot(driver);

        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
    }

    private void addHome() {
        List<WebElement> accountCreation = driver.findElements(By.className("android.view.View"));
        List<WebElement> buttons = accountCreation.stream().filter(element -> element.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        List<WebElement> button = buttons.stream().filter(Object -> Objects.requireNonNull(Object.getDomAttribute("content-desc")).startsWith("Create a new smart home")).collect(Collectors.toList());
        if (!button.isEmpty()) button.get(0).click();
        AppUtil.captureScreenshot(driver);
//        ActionsUtil.Tap.withPercentage(driver, 0.50, 0.75); // Narzo 0.50, 0.625 Tab 0.50, 0.75
        AppUtil.captureScreenshot(driver);
        WebElement HomeName = driver.findElement(By.xpath("//android.widget.EditText"));
        HomeName.click();
        HomeName.sendKeys("Script");
        AppUtil.captureScreenshot(driver);
        WebElement Create = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Create\"]"));
        Create.click();
        AppUtil.captureScreenshot(driver);
        ActionsUtil.sleep(5000);
    }

    private void home() {
        WebElement FamilyEdit = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[2]"));
        FamilyEdit.click();
        System.out.println("Family Edit");
        AppUtil.captureScreenshot(driver);

        WebElement LeaveHome = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Leave home\"]"));
        LeaveHome.click();
        System.out.println("Leave home");
        AppUtil.captureScreenshot(driver);

        WebElement Cancel = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel.click();
        System.out.println("Cancel");
        AppUtil.captureScreenshot(driver);

        WebElement delete = null;
        try {
            delete = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Delete home\"]"));
        } catch (Exception ignored) {
        }
        if (delete != null) {
            delete.click();
            Cancel = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
            Cancel.click(); // android.widget.Button[@content-desc="Cancel"]
        }
    }

    private void countMember() {
        List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
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
                manageFamily = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
            } catch (Exception ignored) {
            }
            if (manageFamily == null) driver.navigate().back();
        }
    }

    private void checkLogout() {
        ScreenCheck screen = new ScreenCheck(driver);
        screen.moreTab();
        List<WebElement> moreTab;
        do {
            List<WebElement> MT = driver.findElements(By.className("android.view.View"));
            System.out.println(MT.size());
            List<WebElement> mt = MT.stream().filter(webElement -> webElement.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println(mt.size());
            moreTab = mt.stream().filter(webElement -> Objects.equals(webElement.getDomAttribute("content-desc"), "Logout")).collect(Collectors.toList());
            System.out.println(moreTab.size());
            if (moreTab.isEmpty()) {
                ActionsUtil.Scroll.Up(driver);
            } else if (!moreTab.get(0).isDisplayed()) // Rare case where the Logout button is in the DOM but not on the Screen
            {
                System.out.println(!moreTab.get(0).isDisplayed());
                ActionsUtil.Scroll.Up(driver);
            }
        } while (moreTab.isEmpty());
    }
}

