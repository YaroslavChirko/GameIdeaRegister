package gamereg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConceptDao {

	private Connection conn;
	
	public ConceptDao (Connection conn) {
		this.conn = conn;
		init();
	}
	
	private void init() {
		createTables();
	}
	
	private void createTables() {
	
		String createConceptTableStr = "Create Table If Not Exists Concepts("
				+ "id INT SERIAL NOT NULL,"
				+ "title VARCHAR(120) NOT NULL, "
				+ "description VARCHAR(2000),"
				+ "character_id INT,"
				+ "FOREIGN KEY (character_id) REFERENCES Characters(id))";
		
		String createPlayerTable="";
		
		try(PreparedStatement createStatement = conn.prepareStatement(createConceptTableStr)){
			createStatement.addBatch();
		}catch(SQLException e) {
			System.out.println("SQLException occured in createTables: " + e.getMessage());
		}
	}
	
}
