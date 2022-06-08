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
		int typeValue = JDBCType.valueOf(typeName).ordinal();
		String result = null;
		switch(typeValue) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				result =  java.lang.String.class.getName();
			
			case Types.INTEGER:
				result =  java.lang.Integer.class.getName();
		
			case Types.FLOAT:
				result =  java.lang.Float.class.getName();
				
			case Types.DOUBLE:
				result =  java.lang.Double.class.getName();
				
			case Types.BOOLEAN:
				result =  java.lang.Boolean.class.getName();
			
			case Types.DATE:
				result =  java.time.LocalDate.class.getName();
				
			case Types.TIME:
				result =  java.time.LocalTime.class.getName();
			
			case Types.TIMESTAMP:
				result =  java.time.LocalDateTime.class.getName();
		
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
		
		switch(className) {
			case "java.lang.String":
				result =  JDBCType.VARCHAR;
			case "java.lang.Integer":
				result =  JDBCType.INTEGER;
			case "java.lang.Float":
				result =  JDBCType.FLOAT;
			case "java.lang.Double":
				result =  JDBCType.DOUBLE;
			case "java.lang.Boolean":
				result =  JDBCType.BOOLEAN;
			case "java.time.LocalDate":
			case "java.sql.Date":
				result =  JDBCType.DATE;
			case "java.time.LocalTime":
			case "java.sql.Time":
				result =  JDBCType.TIME;
			case "java.time.LocalDateTime":
			case "java.sql.Timestamp":
				result =  JDBCType.TIMESTAMP;
				
		}
		
		return result;
	}
	
}
