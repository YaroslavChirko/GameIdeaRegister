package gamereg.dao.models;

import gamereg.dao.annotations.Column;
import gamereg.dao.annotations.Table;

/*
 * even though this class implements GameCharacter interface it would have it's own table for easier table creation
 * */
@Table
public class Player implements GameCharacter {

	//auto generated on the database
	@Column(pk = true, unique = true, nullable = false, autogenerated=true)
	private int id;
	
	//Should add checks for length of fields
	@Column(name = "name", size = 20)
	private String name;
	@Column(name = "powers", size = 500)
	private String powers;
	@Column(name = "appearance", size = 500)
	private String appearance;
	@Column(name = "story", size = 2000)
	private String story;
	
	
	public Player() {}
	
	public Player(int id, String name, String powers, String appearance, String story) {
		this.id = id;
		this.name = name;
		this.powers = powers;
		this.appearance = appearance;
		this.story = story;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getPowers() {
		return powers;
	}
	
	
	public void setPowers(String powers) {
		this.powers = powers;
	}
	
	@Override
	public String getAppearance() {
		return appearance;
	}
	
	public void setAppearance(String appearance) {
		this.appearance = appearance;
	}
	
	public String getStory() {
		return story;
	}
	
	public void setStory(String story) {
		this.story = story;
	}

	@Override
	public String toString() {
		return "Player: "+name+",\nlooks like: "+appearance+",\n"+story+"\nspecial abilities: "+powers;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if(obj.getClass().equals(this.getClass())) {
			Player otherP = (Player)obj;
			result = this.id==otherP.id && this.name.equals(otherP.getName()) 
					&& this.powers.equals(otherP.getPowers())
					&& this.appearance.equals(otherP.getAppearance())
					&&this.story.equals(otherP.getStory());
		}
		
		return result;
	}
	
	@Override
	public int hashCode() {
		int result = 9;
		int prime = 29;
		
		result = prime * result + id;
		result = prime * result + name.hashCode();
		result = prime * result + powers.hashCode();
		result = prime * result + appearance.hashCode();
		result = prime * result + story.hashCode();
		
		return result;
	}
	
	
	

}
