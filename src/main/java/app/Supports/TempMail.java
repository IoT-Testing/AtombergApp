package app.Supports;

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
    public static void main(String[] args) throws IOException, UnsupportedFlavorException {
        TempMail.Email();
    }
    public static String mail;
    public static String OTP;
    static WebDriver driver1 = new ChromeDriver();

    public static String Email() throws IOException, UnsupportedFlavorException {

        driver1.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver1.get("https://temp-mail.org/en/");
        System.out.println("Website Opened");
        WebElement copyButton = driver1.findElement(By.xpath("//body/div[1]/div[1]/div[1]/div[2]/div[1]/form[1]/div[2]/button[1]"));
        copyButton.click();
        mail = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        System.out.println(mail);
        return mail;
    }

    public static String OTP() {
        WebElement emailtap = null;
        do {
            try {
                emailtap = driver1.findElement(By.xpath("//body/div[1]/div[1]/div[1]/div[2]/div[1]/form[1]/div[2]/button[1]"));
            } catch (Exception e) {
            }
        } while (emailtap != null);
        WebElement otp = driver1.findElement(By.tagName("h2"));
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


