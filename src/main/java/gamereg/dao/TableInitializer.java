package gamereg.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class TableInitializer {
	
	public static void initTables(Connection conn) {
		//genre is not an enum yet, though I can fix it later
		String createConceptTableStr = "CREATE TABLE IF NOT EXISTS Concepts("
				+ "title VARCHAR(120) NOT NULL UNIQUE PRIMARY KEY, "
				+ "description VARCHAR(2000),"
				+ "genre VARCHAR(20));";
		
		String createPlayerTableStr="CREATE TABLE IF NOT EXISTS Players("
				+ "id INT SERIAL NOT NULL,"
				+ "name VARCHAR(30),"
				+ "powers VARCHAR(250),"
				+ "appearance VARCHAR(1000),"
				+ "story VARCHAR(2000),"
				+ "game_id INT NOT NULL,"
				+ "FOREIGN KEY game_id REFERENCES Concepts(id));";
		
		String createEnemyTableStr="CREATE TABLE IF NOT EXISTS Enemies("
				+ "id INT SERIAL NOT NULL,"
				+ "name VARCHAR(30),"
				+ "powers VARCHAR(250),"
				+ "appearance VARCHAR(1000),"
				+ "move_pattern VARCHAR(2000),"
				+ "game_id INT NOT NULL,"
				+ "FOREIGN KEY game_id REFERENCES Concepts(id));";
		
		try(Statement createStatement = conn.createStatement()){
			createStatement.addBatch(createConceptTableStr);
			createStatement.addBatch(createPlayerTableStr);
			createStatement.addBatch(createEnemyTableStr);
			
			//for now I don't check which one gone wrong
			createStatement.executeBatch();
		}catch(SQLException e) {
			System.out.println("SQLException occured in createTables: " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
