package singleton;

//stores data per session
public class SessionData {
	
	private static String macAddress; //mac address of opponent to send to
	private static String characterClass;
	private static boolean chosen = false;
	private static boolean opponentChosen = false;
	private static int[] varOp, varMe;
	private static String myyClass;
	private static boolean getsFirstTurn; // decided by who sent the first NFC challenge
	private static boolean isLoggedIn;
	private static String savedUsername;
	private static String savedPassword;

	private static String webServiceAddress = "";
	
	private static int opponentLevel;
	
	//opponent username (for challenge)
	private static String opponentUsername;
	
	
	
	
	public static int getOpponentLevel() {
		return opponentLevel;
	}

	public static void setOpponentLevel(int opponentLevel) {
		SessionData.opponentLevel = opponentLevel;
	}

	public static String getOpponentUsername() {
		return opponentUsername;
	}

	public static void setOpponentUsername(String opponentUsername) {
		SessionData.opponentUsername = opponentUsername;
	}

	public static String getWebServiceIp() {
		return webServiceAddress;
	}

	public static void setWebServiceIp(String webServiceAddress) {
		SessionData.webServiceAddress = webServiceAddress;
	}

	public static String getSavedUsername() {
		return savedUsername;
	}

	public static void setSavedUsername(String savedUsername) {
		SessionData.savedUsername = savedUsername;
	}

	public static String getSavedPassword() {
		return savedPassword;
	}

	public static void setSavedPassword(String savedPassword) {
		SessionData.savedPassword = savedPassword;
	}

	public static boolean isLoggedIn() {
		return isLoggedIn;
	}

	public static void setLoggedIn(boolean isLoggedIn) {
		SessionData.isLoggedIn = isLoggedIn;
	}

	public static String getMacAddress() {
		return macAddress;
	}

	public static void setMacAddress(String macAddress) {
		SessionData.macAddress = macAddress;
	}
	
	public static void setOpponentClass(String characterClass)
	{
		SessionData.characterClass = characterClass;
	}
	
	public static String getOpponentClass()
	{
		return SessionData.characterClass;
	}
	
	public static void setChosen(boolean chosen)
	{
		SessionData.chosen = chosen;
	}
	
	public static boolean getChosen()
	{
		return SessionData.chosen;
	}
	
	public static void setOpponentChosen(boolean chosen)
	{
		SessionData.opponentChosen = chosen;
	}
	
	public static boolean getOpponentChosen()
	{
		return SessionData.opponentChosen;
	}
	
	public static void setMyClass(String myClass)
	{
		myyClass = myClass;
	}
	
	public static String getMyClass()
	{
		return myyClass;
	}
	
	public static void setMyStats(String health, String defense, String magicLeft)
	{
		try{
			int healthInt = Integer.parseInt(health);
			int defenseInt = Integer.parseInt(defense);
			int magicLeftInt = Integer.parseInt(magicLeft);
			
			varMe = new int[]{healthInt,defenseInt,magicLeftInt};
			} catch(NumberFormatException e)
			{
				e.printStackTrace();
			}
	}
	
	public static void setMyStats(int health, int defense, int magicLeft, int ap)
	{
		varMe = new int[]{health,defense,magicLeft,ap};
	}
	
	public static int[] getMyStats()
	{
		return varMe;
	}
	
	public static void setOpponentStats(int health, int defense, int magicLeft, int ap)
	{
		varOp = new int[]{health,defense,magicLeft,ap};
	}
	
	public static void setOpponentStats(String health, String defense, String magicLeft)
	{
		try{
		int healthInt = Integer.parseInt(health);
		int defenseInt = Integer.parseInt(defense);
		int magicLeftInt = Integer.parseInt(magicLeft);
		
		varOp = new int[]{healthInt,defenseInt,magicLeftInt};
		} catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
	}
	
	public static int[] getOpponentStats()
	{
		return varOp;
	}
	
	public static boolean isFirstTurn()
	{
		return getsFirstTurn;
	}
	
	public static void setFirstTurn(boolean val)
	{
		getsFirstTurn = val;
	}
	
}
