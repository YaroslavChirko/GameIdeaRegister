package gamereg.dao.models;

import java.util.ArrayList;
import java.util.List;

public class Concept {
	//generated on the db
	private int id;
	private String title;
	private String description;
	private String genre;
	
	private List<GameCharacter> characters;
	
	
	public Concept() {}
	
	public Concept(String title, String description, String genre, List<GameCharacter> characters) {
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.characters = characters;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public List<GameCharacter> getCharacters() {
		return characters;
	}

	public void setCharacters(List<GameCharacter> characters) {
		this.characters = characters;
	}

	@Override
	public String toString() {
		return "Game " + title + "\n" + description + "\n\n, genre:" + genre;
	}
	
	
	
}
