package gamedata;

//could be base abstract class later
public class Stats {
	
	private String statName = "";
	private String statImage = "";
	private int statValue = 0;
	private int statWeight = 0;
	
	//continue at http://androidexample.com/How_To_Create_A_Custom_Listview_-_Android_Example/index.php?view=article_discription&aid=67&aaid=92
	public Stats(String statName, String statImage, int statValue, int statWeight)
	{
		this.statName = statName;
		this.statImage = statImage;
		this.statValue = statValue;
		this.statWeight = statWeight;
	}

	public String getStatName() {
		return statName;
	}

	public void setStatName(String statName) {
		this.statName = statName;
	}

	public String getStatImage() {
		return statImage;
	}

	public void setStatImage(String statImage) {
		this.statImage = statImage;
	}

	public int getStatValue() {
		return statValue;
	}

	public void setStatValue(int statValue) {
		this.statValue = statValue;
	}

	public int getStatWeight() {
		return statWeight;
	}

	public void setStatWeight(int statWeight) {
		this.statWeight = statWeight;
	}
	
	

}
