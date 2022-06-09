package gamereg.dao.models;

import gamereg.dao.annotations.Column;
import gamereg.dao.annotations.Table;

@Table
public class Enemy implements GameCharacter {

	@Column(pk = true, unique = true, nullable = false)
	private int id;
	@Column (size = 20)
	private String name;
	@Column (size = 500)
	private String appearance;
	@Column (size = 500)
	private String powers;
	@Column (size = 500)
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
