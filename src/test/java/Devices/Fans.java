package Devices;

import Actions.Swipe;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public class Fans {
	public static void SixLEDcolorSelect(AndroidDriver driver) {
		WebElement RPlus = null;
		WebElement SPlus = null;
		try {
			RPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Golden Oakwood\"]"));
		} catch (Exception e) {
		}
		try {
			SPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Earth Brown\"]"));
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

	public static void Renesa(AndroidDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 5); // generate a random number between 0 and 5
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Brown and Black\"]"));
				color1.click();
				System.out.println("White & Black");
				break;
			case 1:
				WebElement color2 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"White and Black\"]"));
				color2.click();
				System.out.println("Brown & Black");
				break;
			case 2:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color3 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Midnight Black\"]"));
				color3.click();
				System.out.println("Midnight Black");
				break;
			case 3:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color4 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Pebble Grey\"]"));
				color4.click();
				System.out.println("Pebble Grey");
				break;
			case 4:
				Swipe.Right(driver, 0.9, 0.67);
				sleep(500);
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color5 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Misty Teal\"]"));
				color5.click();
				System.out.println("Misty Teal");
				break;

			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void RenesaPlus(AndroidDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 4); // generate a random number between 0 and 4
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Pearl White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Golden Oakwood\"]"));
				color2.click();
				break;
			case 2:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color3 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Earth Brown\"]"));
				color3.click();
				break;
			case 3:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color4 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Natural Oakwood\"]"));
				color4.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void StudioPlus(AndroidDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 5
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Marble White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Earth Brown\"]"));
				color2.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void Aris(AndroidDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 2
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Marble White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Dark Teakwood\"]"));
				color2.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void Jaguar(AndroidDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 3); // generate a random number between 0 and 5
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Marble White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Regent Gray\"]"));
				color2.click();
				break;
			case 3:
				Swipe.Right(driver, 0.9, 0.67);
				WebElement color3 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Matte Black\"]"));
				color3.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void Erica(AndroidDriver driver) {
		try {
			int randomNumber = (int) (Math.random() * 2); // generate a random number between 0 and 2
			switch (randomNumber) {
			case 0:
				WebElement color1 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Snow White\"]"));
				color1.click();
				break;
			case 1:
				WebElement color2 = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Umber Brown\"]"));
				color2.click();
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
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
