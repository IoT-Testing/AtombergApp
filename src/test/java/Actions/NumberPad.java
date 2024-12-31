package Actions;

import io.appium.java_client.android.AndroidDriver;
// number pad using co-ordinates of each element on the number pad
public class NumberPad {
	public static void one(AndroidDriver driver){
		Tap.withCoordinates(driver,240,1725);
	}
	public static void two(AndroidDriver driver){
		Tap.withCoordinates(driver,530,1725);
	}
	public static void three(AndroidDriver driver){
		Tap.withCoordinates(driver,810,1725);
	}
	public static void four(AndroidDriver driver){
		Tap.withCoordinates(driver,240,1945);
	}
	public static void five(AndroidDriver driver){
		Tap.withCoordinates(driver,530,1945);
	}
	public static void six(AndroidDriver driver){
		Tap.withCoordinates(driver,810,1945);
	}
	public static void seven(AndroidDriver driver){
		Tap.withCoordinates(driver,240,2100);
	}
	public static void eight(AndroidDriver driver){
		Tap.withCoordinates(driver,530,2100);
	}
	public static void nine(AndroidDriver driver){
		Tap.withCoordinates(driver,810,2100);
	}
	public static void zero(AndroidDriver driver){
		Tap.withCoordinates(driver,530,2250);
	}
	public static void clear(AndroidDriver driver){
		Tap.withCoordinates(driver,240,2250);
	}
	public static void done(AndroidDriver driver){
		Tap.withCoordinates(driver,810,2250);
	}
}
