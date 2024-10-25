package app.Connectivity;

import app.ScreenCheck.ScreenCheck;
import app.ScreenCheckCallbackAction;
import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class AIConnectivity {
    public AppiumDriver driver;
    public AIConnectivity(AppiumDriver driver){
        this.driver = driver;
    }
    public void Alexa(){
        Alexa alexa = new Alexa(driver);
        alexa.Connect();
        alexa.Disconnect();
    }
    public void GoogleHome(){
        GoogleHome googleHome = new GoogleHome(driver);

        googleHome.Connect();
        googleHome.Disconnect();
    }
}
