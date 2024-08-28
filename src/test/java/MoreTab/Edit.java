package MoreTab;

import Actions.Tap;
import io.appium.java_client.ios.IOSDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import AtombergTest.Method;
import io.appium.java_client.ios.IOSDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Edit {
	public static void Profile(IOSDriver driver)
	{

		List<WebElement> ELEMENTS = driver.findElements(By.className("XCUIElementTypeImage"));
		List<WebElement> elements = ELEMENTS.stream().filter(element -> element.getAttribute("name") != null).collect(Collectors.toList());
		System.out.println(elements.size());
		List<WebElement> ele = elements.stream().filter(element -> element.getAttribute("name").startsWith("Hi,")).collect(Collectors.toList());
		for (WebElement e : ele) {
			System.out.println(e.getAttribute("name"));
			if (e.getAttribute("name").startsWith("Hi,")) {
				e.click();
				System.out.println("Edit Profile");
			}
		}
		sleep(3000);

		WebElement ChangeAvatar = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Change avatar\"]"));
		ChangeAvatar.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap On Change Avatar");
		for (int i = 1; i < 25; i++) {
			WebElement Avatar1 = driver.findElement(By.xpath("//XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeImage["+i+"]"));
			Avatar1.click();
			Method.captureScreenshot(driver);
		}
		driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Continue\"]")).click();
		WebElement editName = driver.findElement(By.xpath("//XCUIElementTypeTextField[@index=\"2\"]"));
		editName.click();
		editName.clear();
		editName.sendKeys("Hi Hi Hi");

		WebElement EditNumber = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"+91\"]"));
		EditNumber.click();
		Method.captureScreenshot(driver);
		driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Update\"]")).click();
		sleep(2500);
		Tap.withCoordinates(driver, 35, 125);

		WebElement editProfile = null;
		try {
			editProfile =driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Edit Profile\"]"));
		}catch (Exception e){}
		if(editProfile!=null){
			Tap.withCoordinates(driver, 35, 125);
		}

	}

	private static void sleep(long millis) {
			Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}

}
