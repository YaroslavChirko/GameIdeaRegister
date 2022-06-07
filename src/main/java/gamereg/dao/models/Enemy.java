package gamereg.dao.models;

public class Enemy implements GameCharacter {

	private int id;
	private String name;
	private String appearance;
	private String powers;
	private String movePattern;
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
	public String getAppearance() {
		return appearance;
	}
	public void setAppearance(String appearance) {
		this.appearance = appearance;
	}
	
	@Override
	public String getPowers() {
		return powers;
	}
	public void setPowers(String powers) {
		this.powers = powers;
	}
	
	public String getMovePattern() {
		return movePattern;
	}
	public void setMovePattern(String movePattern) {
		this.movePattern = movePattern;
	}
	
	

}
