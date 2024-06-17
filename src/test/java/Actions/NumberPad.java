package Actions;

import io.appium.java_client.AppiumDriver;


public class NumberPad {
	public static void one(AppiumDriver driver){
		Tap.withCoordinates(driver,240,1725);
	}
	public static void two(AppiumDriver driver){
		Tap.withCoordinates(driver,530,1725);
	}
	public static void three(AppiumDriver driver){
		Tap.withCoordinates(driver,810,1725);
	}
	public static void four(AppiumDriver driver){
		Tap.withCoordinates(driver,240,1945);
	}
	public static void five(AppiumDriver driver){
		Tap.withCoordinates(driver,530,1945);
	}
	public static void six(AppiumDriver driver){
		Tap.withCoordinates(driver,810,1945);
	}
	public static void seven(AppiumDriver driver){
		Tap.withCoordinates(driver,240,2100);
	}
	public static void eight(AppiumDriver driver){
		Tap.withCoordinates(driver,530,2100);
	}
	public static void nine(AppiumDriver driver){
		Tap.withCoordinates(driver,810,2100);
	}
	public static void zero(AppiumDriver driver){
		Tap.withCoordinates(driver,530,2250);
	}
	public static void clear(AppiumDriver driver){
		Tap.withCoordinates(driver,240,2250);
	}
	public static void done(AppiumDriver driver){
		Tap.withCoordinates(driver,810,2250);
	}
}
