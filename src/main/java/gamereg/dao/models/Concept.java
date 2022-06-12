package gamereg.dao.models;

import java.util.List;

import gamereg.dao.annotations.Column;
import gamereg.dao.annotations.Table;

@Table
public class Concept {
	//title must be unique, therefore it is used as Primary Key
	@Column(pk = true, unique = true, nullable = false, size = 100)
	private String title;
	@Column(size = 2000)
	private String description;
	@Column(nullable = false, size = 20)
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
	
	public Genre getGenreEnum() {
		return genre;
	}
	
	public void setGenre(Genre genre) {
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
