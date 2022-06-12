package gamereg.dao;

import java.sql.JDBCType;
import java.sql.Types;

public class JavaTypeSQLTypeMapper {

	/**
	 * Could be used with some data types, if it's not present returns null
	 * 
	 * @param typeName
	 * @return class name 
	 */
	public static String mapSQLToJava(String typeName) {
		JDBCType typeValue = JDBCType.valueOf(typeName);
		String result = null;
		switch(typeValue) {
			case CHAR:
			case VARCHAR:
			case LONGVARCHAR:
				result =  java.lang.String.class.getName();
				break;
			case INTEGER:
				result =  int.class.getName();
				break;
			case FLOAT:
				result =  float.class.getName();
				break;
			case DOUBLE:
				result =  double.class.getName();
				break;
			case BOOLEAN:
				result = boolean.class.getName();
				break;
			case DATE:
				result =  java.time.LocalDate.class.getName();
				break;
			case TIME:
				result =  java.time.LocalTime.class.getName();
				break;
			case TIMESTAMP:
				result =  java.time.LocalDateTime.class.getName();
				break;
		}
		
		return result;
			
	}
	 /**
	  * Could be used with some data types, if type is not supported null is returned
	  * @param className
	  * @return JDBCType representation of passed name
	  */
	public static JDBCType mapJavaToSQL(String className) {
			
			JDBCType result = null;
			
			if(className.equals("java.lang.String") || className.equals("gamereg.dao.models.Concept$Genre")) {
				result =  JDBCType.VARCHAR;
			}else if(className.equals("java.lang.Integer") || className.equals("int")) {
				result =  JDBCType.INTEGER;
			}else if(className.equals("java.lang.Float") || className.equals("float")) {
				result =  JDBCType.FLOAT;
			}else if(className.equals("java.lang.Double") || className.equals("double")) {
				result =  JDBCType.DOUBLE;
			}else if(className.equals("java.lang.Boolean") || className.equals("boolean")) {
				result =  JDBCType.BOOLEAN;
			}else if(className.equals("java.time.LocalDate")||className.contains("java.sql.Date")) {
				result =  JDBCType.DATE;
			}else if(className.equals("java.time.LocalTime") || className.equals("java.sql.Time")) {
				result =  JDBCType.TIME;
			}else if(className.equals("java.time.LocalDateTime") || className.equals("java.sql.Timestamp")) {
				result =  JDBCType.TIMESTAMP;
			}
			
			return result;
	}
	
}
