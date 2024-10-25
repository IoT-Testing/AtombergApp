package app;

import app.MoreTab.Help;
import app.MoreTab.Manage;
import app.MoreTab.Play;
import app.MoreTab.Profile;
import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.ReadFromCSV;
import io.appium.java_client.AppiumDriver;
import java.util.List;

public class AutomatedTest {

    public AppiumDriver driver;
    public void run() {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        driver = appInitializer.getDriver();
        appInitializer.checkMainScreen();
        Help help = new Help(driver);
        Profile profile = new Profile(driver);
        Manage manage = new Manage(driver);
        Play play = new Play(driver);
        ScreenCheck screen = new ScreenCheck(driver);
        List<List<String>> credentials = ReadFromCSV.readFromCSV();
        for (List<String> credential : credentials) {
            String email = credential.get(0);
            String password = credential.get(1);
            appInitializer.login(email, password);
            ActionsUtil.sleep(1000);
            screen.moreTab();
            profile.edit();
            manage.theme();
            manage.electricityUnitPrice();
            manage.changeCurrency();
            driver.navigate().back();
            driver.navigate().back();
            manage.family();
            manage.help();
            help.raiseAComplaint();
            help.trackAComplaint();
            play.videos();
            help.manual();//works properly till here : 22/10/2024: 15:00
            help.troubleshoot();
            ActionsUtil.Scroll.Up(driver);
            help.email();
            help.call();//works properly till here : 22/10/2024: 16:00
            driver.navigate().back();
            ActionsUtil.Scroll.Up(driver);
            manage.changePassword();
            manage.deleteAccount();
            ActionsUtil.Scroll.Up(driver);
            manage.developerOptions();
            manage.logout();
            ActionsUtil.sleep(2000);
        }
    }
}
