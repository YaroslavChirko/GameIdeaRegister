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
	
	public Enemy() {}
	
	public Enemy(int id, String name, String appearance, String powers, String movePattern) {
		this.id = id;
		this.name = name;
		this.appearance = appearance;
		this.powers = powers;
		this.movePattern = movePattern;
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
	
	
	
	@Override
	public String toString() {
		return "Enemy: "+name+",\nlooks like: "+appearance+",\ncan: "+powers+"\nmove pattern: "+movePattern;
		
		
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if(obj.getClass().equals(this.getClass())) {
			Enemy otherE = (Enemy)obj;
			result = this.id==otherE.id && this.name.equals(otherE.getName()) 
					&& this.powers.equals(otherE.getPowers())
					&& this.appearance.equals(otherE.getAppearance())
					&&this.movePattern.equals(otherE.getMovePattern());
		}
		
		return result;
	}

	@Override
	public int hashCode() {
		int result = 7;
		int prime = 23;
		
		result = prime * result + id;
		result = prime * result + name.hashCode();
		result = prime * result + powers.hashCode();
		result = prime * result + appearance.hashCode();
		result = prime * result + movePattern.hashCode();
		
		return result;
	}

	
	

}
