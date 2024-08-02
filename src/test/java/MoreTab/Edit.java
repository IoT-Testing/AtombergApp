package MoreTab;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Edit {
	public static void Profile(AppiumDriver driver)
	{

		List<WebElement> ELEMENTS = driver.findElements(By.className("android.widget.ImageView"));
		List<WebElement> elements = ELEMENTS.stream().filter(element -> element.getAttribute("content-desc") != null).collect(Collectors.toList());
		System.out.println(elements.size());
		for (WebElement e : elements) {
			if (e.getAttribute("content-desc").startsWith("Hi,")) {
				e.click();
				System.out.println("Edit Profile");
			}
		}
		WebElement ChangeAvatar = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Change avatar\"]"));
		ChangeAvatar.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap On Change Avatar");
		for (int i = 1; i < 25; i++) {
			WebElement Avatar1 = driver.findElement(By.xpath("//android.widget.ScrollView/android.view.View[2]/android.view.View/android.view.View/android.widget.ImageView[" + i + "]"));
			Avatar1.click();
			Method.captureScreenshot(driver);
		}
		driver.navigate().back();
		WebElement editName = driver.findElement(By.xpath("//android.widget.EditText[@index=\"1\"]"));
		editName.click();
		editName.clear();
		editName.sendKeys("Hi Hi Hi");

		WebElement EditNumber = driver.findElement(By.xpath("//android.view.View[@content-desc=\"+91\"]"));
		EditNumber.click();
		Method.captureScreenshot(driver);
		driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Update\"]")).click();
		sleep(250);
		driver.navigate().back();
		driver.navigate().back();

	}

		private static void sleep(long millis) {
			Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}
}
