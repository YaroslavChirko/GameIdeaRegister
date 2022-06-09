package gamereg.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;

import gamereg.dao.annotations.Column;
import gamereg.dao.models.Concept;
import gamereg.dao.models.Enemy;
import gamereg.dao.models.Player;

import java.sql.SQLException;

public class TableInitializer {
	
	public static String columnCreationLine(Field field, Column column) {
		StringBuilder columnLine = new StringBuilder();
		columnLine.append(AnnotationFunctions.getFieldNameFromAnnotation(field, column));
		
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
		Class<? extends Object> objClass = obj.getClass();
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
		String conceptsName = AnnotationFunctions.getTableNameFromAnnotation(Concept.class);
		String createConceptTableStr = writeTableCreateString(new Concept(), conceptsName, false);

		try {
			Field conceptTitleField = Concept.class.getField("title");
			String conceptTitleColumn = AnnotationFunctions.getFieldNameFromAnnotation(conceptTitleField);
			
			String playersName = AnnotationFunctions.getTableNameFromAnnotation(Player.class);
			String createPlayerTableStr= writeTableCreateString(new Player(),playersName,true);
			createPlayerTableStr += "game_name INT NOT NULL, FOREIGN KEY game_name REFERENCES"+ conceptsName+"("+conceptTitleColumn+"));";//might add an annotation for foreign key but most probably won't
			
			String enemyName = AnnotationFunctions.getTableNameFromAnnotation(Enemy.class);
			String createEnemyTableStr=writeTableCreateString( new Enemy(), enemyName, true);				
			createEnemyTableStr += "game_name INT NOT NULL, FOREIGN KEY game_name REFERENCES"+ conceptsName+"("+conceptTitleColumn+"));";
			
			Statement createStatement = conn.createStatement();
				createStatement.addBatch(createConceptTableStr.toString());
				createStatement.addBatch(createPlayerTableStr);
				createStatement.addBatch(createEnemyTableStr);
				
				//for now I don't check which one gone wrong
				createStatement.executeBatch();
			
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			System.out.println("SQLException occured in createTables: " + e.getMessage());
			throw new RuntimeException(e);
		}
		
		
		
	}
}
