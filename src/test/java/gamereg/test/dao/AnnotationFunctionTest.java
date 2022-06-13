package gamereg.test.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Test;

import gamereg.dao.AnnotationFunctions;
import gamereg.dao.annotations.Column;
import gamereg.dao.annotations.Table;
import gamereg.dao.models.Concept;


public class AnnotationFunctionTest {
	
	final String name = "from_annotation";
	@Test
	public void testGetTableNameFromAnnotation(){
		//the class is annotated so we'll get the proper line, the annotation has no name argument so name of table will be simple name of class 
		String tableName = AnnotationFunctions.getTableNameFromAnnotation(Concept.class);
		assertEquals(Concept.class.getSimpleName() , tableName);
		
		final String finalTableName = "Specific"; 
		@Table(name=finalTableName)
		class TestOne{}
		assertEquals(finalTableName, AnnotationFunctions.getTableNameFromAnnotation(TestOne.class));
	}
	
	@Test
	public void testGetFieldNameFromAnnotation() throws NoSuchFieldException, SecurityException {
		
		String funcResult;
		
		funcResult = AnnotationFunctions.getFieldNameFromAnnotation(FieldTest.class.getDeclaredField("notAnnotated"));
		assertEquals(null, funcResult);
		
		Field annotatedField = FieldTest.class.getDeclaredField("annotatedOne");
		funcResult = AnnotationFunctions.getFieldNameFromAnnotation(annotatedField);
		assertEquals(annotatedField.getName(), funcResult);
		
		Field annotatedWithName = FieldTest.class.getDeclaredField("annotatedWithName");
		funcResult = AnnotationFunctions.getFieldNameFromAnnotation(annotatedWithName);
		assertEquals(name, funcResult);
		
	}
	
	@Test
	public void testGetRowNameByFieldName() {
		assertEquals(null, AnnotationFunctions.getRowNameByFieldName("notAnnotated", FieldTest.class));
		assertEquals("annotatedOne", AnnotationFunctions.getRowNameByFieldName("annotatedOne", FieldTest.class));
		assertEquals(name, AnnotationFunctions.getRowNameByFieldName("annotatedWithName", FieldTest.class));
		
		
	}
	
	@Test
	public void testGetFieldNameByRowName() {
		assertNotEquals("notAnnotated", AnnotationFunctions.getFieldNameByRowName("notAnnotated", FieldTest.class));//just to make something different
		assertEquals("annotatedOne", AnnotationFunctions.getFieldNameByRowName("annotatedOne", FieldTest.class));
		assertEquals("annotatedWithName", AnnotationFunctions.getFieldNameByRowName(name, FieldTest.class));
	}
	
	@Test
	public void testIsFieldAnnotated() {
		assertFalse(AnnotationFunctions.isFieldColumnAnnotated("notAnnotated", FieldTest.class)); //any name can be used but we chose the name of field
		assertTrue(AnnotationFunctions.isFieldColumnAnnotated("annotatedOne", FieldTest.class));
		
	}
	
	
	
	private class FieldTest{
		private String notAnnotated;
		
		@Column
		private boolean annotatedOne;
		
		@Column(name=name)
		private boolean annotatedWithName;
	}
}
