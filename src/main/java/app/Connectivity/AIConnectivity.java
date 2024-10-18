package app.Connectivity;

import io.appium.java_client.AppiumDriver;

import java.util.List;
import java.util.Map;

public class AIConnectivity {
    public AppiumDriver driver;
    public AIConnectivity(AppiumDriver driver){
        this.driver = driver;
        driver.executeScript("mobile:shell",
                Map.of("command", "pm", "args", List.of("clear", "com.android.chrome")));

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
