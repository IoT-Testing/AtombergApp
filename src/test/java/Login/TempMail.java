package Login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;


public class TempMail {
    public static String Email;
    public static String OTP;
    static WebDriver driver = new ChromeDriver();

    public static String Email() throws IOException, UnsupportedFlavorException {

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.get("https://temp-mail.org/en/");
        System.out.println("Website Opened");
        WebElement copyButton = driver.findElement(By.xpath("//body/div[1]/div[1]/div[1]/div[2]/div[1]/form[1]/div[2]/button[1]"));
        copyButton.click();
        Email = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        System.out.println(Email);
        return Email;
    }

    public static String OTP() {
        WebElement emailtap = null;
        do {
            try {
                emailtap = driver.findElement(By.xpath("//body/div[1]/div[1]/div[1]/div[2]/div[1]/form[1]/div[2]/button[1]"));
            } catch (Exception e) {
            }
        } while (emailtap != null);
        WebElement otp = driver.findElement(By.tagName("h2"));
        OTP = otp.getText();
        System.out.println(OTP);
        return OTP;
    }

    private static void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


