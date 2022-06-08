package gamereg.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;

import gamereg.dao.models.Concept;
import gamereg.dao.models.Enemy;
import gamereg.dao.models.Player;

import java.sql.SQLException;

public class TableInitializer {
	
	public static String columnCreationLine(Field field, Column column) {
		StringBuilder columnLine = new StringBuilder();
		if(column.name().equals("")) {
			columnLine.append(field.getName());
		}else {
			columnLine.append(column.name());
		}
		
		String type = field.getType().getName();
		
		columnLine.append(" ");
		columnLine.append(JavaTypeSQLTypeMapper.mapJavaToSQL(type).getName());
		if(type.equals("java.lang.String")) {
			columnLine.append(" ");
			columnLine.append("("+column.size()+")");
		}
		
		if(!column.nullable()) {
			columnLine.append(" ");
			columnLine.append("NOT NULL");
		}
		
		if(column.pk()) {
			columnLine.append(" ");
			columnLine.append("PRIMARY KEY");
		}
		
		if(column.unique()) {
			columnLine.append(" ");
			columnLine.append("UNIQUE");
		}
		
		columnLine.append(",");
		return columnLine.toString();
	}
	
	public static String writeTableCreateString(Object obj, String tableName, boolean hasForeign) {
		StringBuilder createTableStr = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		createTableStr.append(tableName+"(");
		Class objClass = obj.getClass();
		Field[] objFields = objClass.getFields();
		for(Field objField : objFields) {
			Column col = objField.getAnnotation(Column.class);
			if(col != null) {
				createTableStr.append(columnCreationLine(objField, col));
			}else {
				continue;
			}
			
		}
		
		
		if(hasForeign) {
			return createTableStr.toString();
		}else {
			return createTableStr.substring(0,createTableStr.lastIndexOf(","))+");";
		}
		
	}
	
	public static void initTables(Connection conn) {
		String createConceptTableStr = writeTableCreateString(new Concept(), "Concepts", false);

		String createPlayerTableStr= writeTableCreateString(new Player(), "Players",true);
		createPlayerTableStr += "game_id INT NOT NULL, FOREIGN KEY game_id REFERENCES Concepts(id));";
		
		String createEnemyTableStr=writeTableCreateString( new Enemy(), "Enemies", true);				
		createEnemyTableStr += "game_id INT NOT NULL, FOREIGN KEY game_id REFERENCES Concepts(id));";
		
		try(Statement createStatement = conn.createStatement()){
			createStatement.addBatch(createConceptTableStr.toString());
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
