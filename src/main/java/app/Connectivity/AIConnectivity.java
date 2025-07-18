package app.Connectivity;

import io.appium.java_client.android.AndroidDriver;

public class AIConnectivity {
    public AndroidDriver driver;
    public AIConnectivity(AndroidDriver driver){
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
