package app.Login;

import io.appium.java_client.android.AndroidDriver;

public class Login {
	public static void main(AndroidDriver driver) {
		try {

			int randomNumber = (int) (Math.random() * 4); // generate a random number between 0 and 5
			switch (randomNumber) {
			case 0:
				System.out.println("Login with Apple");
				Apple.Login(driver);
				break;
			case 1:
				System.out.println("Login with Email");
				Email.Login(driver);
				break;
			case 2:
				System.out.println("Login with Facebook");
				/* FB.Login(driver); */
				Email.Login(driver);
				break;

			case 3:
				System.out.println("Login with Google");
				/* Google.Login(driver); */
				Apple.Login(driver);
				break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}
}