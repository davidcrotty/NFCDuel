package gamedata;

public class ClassValues {

	//static method to retreive v
	
	public static int[] getValues(String text)
	{
		int[] var = null;
		
		if(text.compareTo("Warrior")==0)
		{
			var = getWarriorValues();
		}else if(text.compareTo("Paladin")==0)
		{
			var = getPaladinValues();
		}else if(text.compareTo("Wizard")==0)
		{
			var = getWizardValues();
		} 
		
		return var;
	}
	
	public static int[] getWarriorValues()
	{
		int[] var= new int[]{100,80,30,80};
		
		
		return var;
	}
	
	public static int[] getPaladinValues()
	{
		int[] var= new int[]{90,60,60,60};
		
		return var;
	}
	
	public static int[] getWizardValues()
	{
		int[] var= new int[]{80,20,90,40};
		
		return var;
	}
	
	public static int[] getStatsBasedOnClass(String classType)
	{
		int[] var;
		
		if(classType.compareTo("Warrior")==0)
		{
			var = getWarriorValues();
		} else if (classType.compareTo("Paladin")==0)
		{
			var = getPaladinValues();
		} else if (classType.compareTo("Wizard")==0)
		{
			var = getWizardValues();
		}else
		{
			var = null;
		}
		
		return var;
	}
}
