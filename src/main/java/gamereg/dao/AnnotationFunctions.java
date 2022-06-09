package gamereg.dao;

import java.lang.reflect.Field;

import gamereg.dao.annotations.Column;
import gamereg.dao.annotations.Table;
import gamereg.dao.models.Concept;

public class AnnotationFunctions {

	public static String getTableNameFromAnnotation(Object entity) {
		return getTableNameFromAnnotation(entity, entity.getClass().getAnnotation(Table.class));
	}
	
	public static String getTableNameFromAnnotation(Object entity, Table table) {
		String nameFromAnnotation = table.name();
		if(nameFromAnnotation.equals(""))  nameFromAnnotation = entity.getClass().getSimpleName();
		
		return nameFromAnnotation;
	}
	
	public static String getFieldNameFromAnnotation(Field field) {
		return  getFieldNameFromAnnotation(field, field.getAnnotation(Column.class));
		
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
	public static String getRowNameByFieldName(String fieldName,Object entity) {
		String rowName = null;
		try {
			Field field = entity.getClass().getField(fieldName);
			Column fieldColumn = field.getAnnotation(Column.class);
			getFieldNameFromAnnotation(field,fieldColumn);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return rowName;
	}
	
	
	/**
	 * Use to get the field name that corresponds to the passed row name.<br>
	 * Is not great performance wise but should do
	 * @param rowName
	 * @return returns name of the corresponding field or null if no annotated field was found
	 */
	public static  String getFieldNameByRowName(String rowName, Object entity) {
		String fieldName = null;
		Field[] fields = entity.getClass().getFields();
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
	
}
