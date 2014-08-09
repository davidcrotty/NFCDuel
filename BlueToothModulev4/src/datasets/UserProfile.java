package datasets;

import com.example.bluetoothmodulev4.R;

public class UserProfile {

	private String username;
	private String firstName;
	private String lastName;
	private int wins;
	private int losses;
	private int level;
	private String rank;
	private String emailAddress;
	
	//make static to convert to drawable
	private int drawable;
	
	//add method that sets drawable based on input rank
	
	public UserProfile(String username, String firstName, String lastName,
			int wins, int losses, int level, String rank, String emailAddress) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.wins = wins;
		this.losses = losses;
		this.level = level;
		this.rank = rank;
		this.emailAddress = emailAddress;
	}

	public void setDrawable(String rank)
	{
		if (rank.compareTo("beginner") == 0) {
			drawable = R.drawable.beginner;
		} else if (rank.compareTo("advanced") == 0) {
			drawable = R.drawable.advanced;
		} else if (rank.compareTo("experienced") == 0) {
			drawable = R.drawable.experienced;
		} else if (rank.compareTo("master") == 0) {
			drawable = R.drawable.master;
		} else if (rank.compareTo("veteran") == 0) {
			drawable = R.drawable.veteran;
		} else if (rank.compareTo("newbie")==0)
		{
			drawable = R.drawable.newbie;
		}
	}
	
	public int getDrawable()
	{
		return this.drawable;
	}

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public int getWins() {
		return wins;
	}


	public void setWins(int wins) {
		this.wins = wins;
	}


	public int getLosses() {
		return losses;
	}


	public void setLosses(int losses) {
		this.losses = losses;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public String getRank() {
		return rank;
	}


	public void setRank(String rank) {
		this.rank = rank;
	}


	public String getEmailAddress() {
		return emailAddress;
	}


	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	
	
}
