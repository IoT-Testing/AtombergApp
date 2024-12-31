//package AtombergTest;
//import io.appium.java_client.android.AndroidDriver;
//import org.json.JSONObject;
//import io.appium.java_client.android.AndroidDriver;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//
//public class STFAppiumExample {
//    private static final String STF_SERVICE_URL = "https://your-stf-instance.com";
//    private static final String ACCESS_TOKEN = "your-access-token";
//    public static AndroidDriver driver;
//    public static void main(String[] args) throws MalformedURLException {
//        // Create a new AndroidDriver instance
//
//        // Use the STF REST API to reserve a device
//        JSONObject deviceResponse = new JSONObject();
//        deviceResponse.put("deviceName", "");
//        deviceResponse.put("platformName", "Android");
//        String deviceId = reserveDevice(STF_SERVICE_URL, ACCESS_TOKEN, deviceResponse);
//
//        // Use the reserved device to run your Appium test
//        driver = new AndroidDriver(new URL(STF_SERVICE_URL + "/wd/hub/session/" + deviceId), capabilities);
//        // Run your test logic here
//        driver.quit();
//    }
//
//    private static String reserveDevice(String stfServiceUrl, String accessToken, JSONObject deviceRequest) {
//        // Send a POST request to the STF REST API to reserve a device
//        URL url = new URL(stfServiceUrl + "/devices");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setDoOutput(true);
//
//        OutputStream os = conn.getOutputStream();
//        os.write(deviceRequest.toString().getBytes());
//        os.close();
//
//        int responseCode = conn.getResponseCode();
//        if (responseCode == 200) {
//            // Parse the response JSON to get the device ID
//            JSONObject responseJson = new JSONObject(conn.getInputStream());
//            return responseJson.getString("deviceId");
//        } else {
//            // Handle error
//        }
//        return null;
//    }
//}