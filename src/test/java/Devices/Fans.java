package Devices;

import Actions.Swipe;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.ios.IOSDriver;

public class Fans {
	public static void SixLEDColorSelect(IOSDriver driver) {
		WebElement RPlus = null;
		WebElement SPlus = null;
		try {
			RPlus = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Golden Oakwood\"]"));
		} catch (Exception e) {
		}
		try {
			SPlus = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Earth Brown\"]"));
		} catch (Exception e) {
		}
		if (RPlus != null) {
			RenesaPlus(driver);
		} else if (SPlus != null) {
			StudioPlus(driver);
		} else {
			Renesa(driver);
		}
		
	}

	public static void Renesa(IOSDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 5); // generate a random number between 0 and 5
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Brown and Black\"]"));
				color1.click();
				System.out.println("White & Black");
				break;
			case 1:
				WebElement color2 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"White and Black\"]"));
				color2.click();
				System.out.println("Brown & Black");
				break;
			case 2:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color3 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Midnight Black\"]"));
				color3.click();
				System.out.println("Midnight Black");
				break;
			case 3:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color4 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Pebble Grey\"]"));
				color4.click();
				System.out.println("Pebble Grey");
				break;
			case 4:
				Swipe.Right(driver, 0.9, 0.67);
				sleep(500);
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color5 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Misty Teal\"]"));
				color5.click();
				System.out.println("Misty Teal");
				break;

			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void RenesaPlus(IOSDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 4); // generate a random number between 0 and 4
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Pearl White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Golden Oakwood\"]"));
				color2.click();
				break;
			case 2:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color3 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Earth Brown\"]"));
				color3.click();
				break;
			case 3:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color4 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Natural Oakwood\"]"));
				color4.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void StudioPlus(IOSDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 5
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Marble White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Earth Brown\"]"));
				color2.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void Aris(IOSDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 2
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Marble White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Dark Teakwood\"]"));
				color2.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void Jaguar(IOSDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 3); // generate a random number between 0 and 5
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Marble White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Regent Gray\"]"));
				color2.click();
				break;
			case 3:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color3 = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Matte Black\"]"));
				color3.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void Erica(IOSDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 2
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Snow White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver
						.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Umber Brown\"]"));
				color2.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
