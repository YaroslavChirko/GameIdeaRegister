package gamereg.test.dao;

import static org.junit.Assert.assertEquals;

import java.sql.JDBCType;
import java.sql.Types;

import org.junit.Test;

import gamereg.dao.JavaTypeSQLTypeMapper;

public class JavaTypeSQLTypeMapperTest {

	@Test
	//shows that it works with both JDBCType and Types
	public void testMapSQLToJava() throws NoSuchFieldException, SecurityException {
		assertEquals(int.class, JavaTypeSQLTypeMapper.mapSQLToJava(Types.class.getField("INTEGER").getName()));
		assertEquals(String.class, JavaTypeSQLTypeMapper.mapSQLToJava(JDBCType.VARCHAR.getName()));
	}
	
}
