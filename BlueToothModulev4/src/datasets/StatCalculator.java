package datasets;

import singleton.LoginData;

public class StatCalculator {

	public static int calcExpgain(int opponentLevel,boolean winOrLose)
	{
		int gain = 0;
		
		if(opponentLevel > LoginData.getLevel())
		{
			gain = LoginData.getLevel() * 180;
		} else if(opponentLevel ==  LoginData.getLevel())
		{
			gain = LoginData.getLevel() * 80;
		} else
		{
			gain = LoginData.getLevel() * 50;
		}
		
		if(!winOrLose)
		{
			gain = gain/2;
		}
		
		return gain;
	}
	
	public static boolean calcLevelUp(int exp, int currentLevel)
	{
		boolean returnVal = false;
		
		if(currentLevel >= exp*100)
		{
			// you can level up
			returnVal = true;
		} else
		{
			
		}
		
		return returnVal;
	}
	
	public static String calcRank(int level)
	{
		String returnRank = "";
		
		if(level < 5)
		{
			//newbie
			returnRank = "newbie";
		} else if(level > 5 && level < 10)
		{
			//beginner
			returnRank = "beginner";
		} else if(level > 10 && level < 15)
		{
			//experienced
			returnRank = "experienced";
		}else if(level > 15 && level < 20)
		{
			//advanced
			returnRank = "advanced";
		}else if(level > 20 && level < 25)
		{
			//master
			returnRank = "master";
		} else if(level > 25)
		{
			//veteran
			returnRank = "veteran";
		}
		
		return returnRank;
	}
	
}
