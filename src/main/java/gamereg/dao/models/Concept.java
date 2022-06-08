package gamereg.dao.models;

import java.util.ArrayList;
import java.util.List;

public class Concept {
	//title must be unique, therefore it is used as Primary Key
	private String title;
	private String description;
	private Genre genre;
	
	private List<GameCharacter> characters;
	
	
	public Concept() {}
	
	public Concept(String title, String description, String genre, List<GameCharacter> characters) {
		this.title = title;
		this.description = description;
		this.genre = Genre.valueOf(genre);
		this.characters = characters;
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
		return genre.toString();
	}

	public void setGenre(String genre) {
		this.genre = Genre.valueOf(genre);
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
	
	
	public enum Genre{
		ACTION,
		ADVENTURE,
		FIGHTING,
		PUZZLE,
		RPG,
		RACING,
		SIM,
		STRATEGY
	}
	
	
}
