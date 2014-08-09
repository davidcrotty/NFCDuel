package singleton;

public class LoginData {

	private static String username = "";
	private static String firstname;
	private static String lastname;
	private static String emailAddress;
	private static int wins;
	private static int losses;
	private static String rank;
	private static int exp;
	private static int level;
	
	public LoginData()
	{
		
	}
	
	public static int getLevel() {
		return level;
	}

	public static void setLevel(int level) {
		LoginData.level = level;
	}



	public static String getUsername() {
		return username;
	}
	public static void setUsername(String username) {
		LoginData.username = username;
	}
	public static String getFirstname() {
		return firstname;
	}
	public static void setFirstname(String firstname) {
		LoginData.firstname = firstname;
	}
	public static String getLastname() {
		return lastname;
	}
	public static void setLastname(String lastname) {
		LoginData.lastname = lastname;
	}
	public static String getEmailAddress() {
		return emailAddress;
	}
	public static void setEmailAddress(String emailAddress) {
		LoginData.emailAddress = emailAddress;
	}
	public static int getWins() {
		return wins;
	}
	public static void setWins(int wins) {
		LoginData.wins = wins;
	}
	public static int getLosses() {
		return losses;
	}
	public static void setLosses(int losses) {
		LoginData.losses = losses;
	}
	public static String getRank() {
		return rank;
	}
	public static void setRank(String rank) {
		LoginData.rank = rank;
	}
	public static int getExp() {
		return exp;
	}
	public static void setExp(int exp) {
		LoginData.exp = exp;
	}
	
	
}
