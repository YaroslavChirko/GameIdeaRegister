package gamereg.dao;

import java.lang.reflect.Field;

import gamereg.dao.annotations.Column;
import gamereg.dao.annotations.Table;

public class AnnotationFunctions {

	public static String getTableNameFromAnnotation(Class<?> entity) {
		String result = null;
		if(entity.isAnnotationPresent(Table.class)) {
			Table t1 = (Table) entity.getAnnotation(Table.class);
			result = getTableNameFromAnnotation(entity, t1);
		}
		
		return result;
	}
	
	public static String getTableNameFromAnnotation(Class<?> entity, Table table) {
		String nameFromAnnotation = table.name();
		if(nameFromAnnotation.equals(""))  nameFromAnnotation = entity.getSimpleName();
		
		return nameFromAnnotation;
	}
	
	public static String getFieldNameFromAnnotation(Field field) {
		String result = null;
		if(field.isAnnotationPresent(Column.class)) result =  getFieldNameFromAnnotation(field, field.getAnnotation(Column.class));
		return result;
	}
	
	public static String getFieldNameFromAnnotation(Field field, Column column) {
		String nameFromAnnotation = column.name();
		
		if(nameFromAnnotation.equals("")) nameFromAnnotation = field.getName();
		
		return nameFromAnnotation;
		
	}
	
	
	
	/**
	 * Use this method to get a name of the row from Column annotation
	 * @param fieldName
	 * @return row name from the annotation
	 */
	public static String getRowNameByFieldName(String fieldName, Class<?> entity) {
		String rowName = null;
		if(AnnotationFunctions.isFieldColumnAnnotated(fieldName, entity)){
			try {
				Field field = entity.getDeclaredField(fieldName);
				Column fieldColumn = field.getAnnotation(Column.class);
				rowName = getFieldNameFromAnnotation(field,fieldColumn);
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return rowName;
	}
	
	
	/**
	 * Use to get the field name that corresponds to the passed row name.<br>
	 * Is not great performance wise but should do
	 * @param rowName
	 * @return returns name of the corresponding field or null if no annotated field was found
	 */
	public static  String getFieldNameByRowName(String rowName, Class<?> entity) {
		String fieldName = null;
		Field[] fields = entity.getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Column.class)) {
				String annotationName =  field.getAnnotation(Column.class).name();
				if(annotationName.equals("")) {
					if(field.getName().equals(rowName)) {
						fieldName = rowName;
					}
				}else {
					if(annotationName.equals(rowName)) {
						fieldName = field.getName();
					}
				}
			}
		}
		return fieldName;	
	}
	
	
	public static boolean isFieldColumnAnnotated(String rowName, Class<?> entity) {
		boolean isAnnotated = false;
		if(!rowName.equals("")) {
			Field[] fields = entity.getDeclaredFields();
			
			for(Field field : fields) {
				if(field.isAnnotationPresent(Column.class) && 
						(rowName.equals(field.getName()) || rowName.equals(field.getAnnotation(Column.class).name()))) {
					isAnnotated = true;
				}
			}
			
		}
		
		return isAnnotated;
	}
	
}
