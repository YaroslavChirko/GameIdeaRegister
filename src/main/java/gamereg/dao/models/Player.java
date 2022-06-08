package gamereg.dao.models;

/*
 * even though this class implements GameCharacter interface it would have it's own table for easier table creation
 * */
public class Player implements GameCharacter {

	//auto generated on the database
	private int id;
	
	//Should add checks for length of fields
	private String playerName;
	private String playerPowers;
	private String playerAppearance;
	private String playerStory;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return playerName;
	}
	
	public void setName(String playerName) {
		this.playerName = playerName;
	}
	
	@Override
	public String getPowers() {
		return playerPowers;
	}
	
	
	public void setPowers(String playerPowers) {
		this.playerPowers = playerPowers;
	}
	
	@Override
	public String getAppearance() {
		return playerAppearance;
	}
	
	public void setAppearance(String playerAppearance) {
		this.playerAppearance = playerAppearance;
	}
	
	public String getStory() {
		return playerStory;
	}
	
	public void setStory(String playerStory) {
		this.playerStory = playerStory;
	}

	@Override
	public String toString() {
		return "Player: "+playerName+",\nlooks like: "+playerAppearance+",\n"+playerStory+"\n special abilities: "+playerPowers;
	}
	
	
	

}
