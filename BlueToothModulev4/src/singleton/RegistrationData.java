package singleton;

public class RegistrationData {

	private static String firstName,lastName,emailAddress,username,password;
	private static boolean wasFragment = false;
	public static String getFirstName() {
		return firstName;
	}
	public static void setFirstName(String firstName) {
		RegistrationData.firstName = firstName;
	}
	public static String getLastName() {
		return lastName;
	}
	public static void setLastName(String lastName) {
		RegistrationData.lastName = lastName;
	}
	public static String getEmailAddress() {
		return emailAddress;
	}
	public static void setEmailAddress(String emailAddress) {
		RegistrationData.emailAddress = emailAddress;
	}
	public static String getUsername() {
		return username;
	}
	public static void setUsername(String username) {
		RegistrationData.username = username;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		RegistrationData.password = password;
	}
	public static boolean isWasFragment() {
		return wasFragment;
	}
	public static void setWasFragment(boolean wasFragment) {
		RegistrationData.wasFragment = wasFragment;
	}
	
	
	
}
