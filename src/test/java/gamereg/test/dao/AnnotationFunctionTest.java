package gamereg.test.dao;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;

import gamereg.dao.AnnotationFunctions;
import gamereg.dao.annotations.Column;
import gamereg.dao.models.Concept;


public class AnnotationFunctionTest {
	
	@Test
	public void testGetTableNameFromAnnotation(){
		String tableName = AnnotationFunctions.getTableNameFromAnnotation(Concept.class);
		assertEquals("Concept" , tableName);
	}
	
	@Test
	public void showFields() {
		Field[] fields = Concept.class.getDeclaredFields();
		for(Field field: fields) {
			if(field.getAnnotation(Column.class) != null) {
				System.out.println(field);
			}
			
		}
		
	}
}
