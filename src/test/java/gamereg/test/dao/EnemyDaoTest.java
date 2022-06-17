package gamereg.test.dao;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import gamereg.dao.EnemyDao;
import gamereg.dao.models.Enemy;
import gamereg.dao.models.GameCharacter;

public class EnemyDaoTest {
	private static Connection connMock;
	private static ResultSetMetaData rsMetaMock;
	
	private static int[] ids = {1, 2};
	private static String[] names= {"Tambo", "Walpe"};
	private static String[] appearances= {"plump dish with eyes on top", "waving stripe of cloth"};
	private static String[] powers= {"sound wave", "spike projectile"};
	private static String[] movePatterns= {"left, left, right, right", "up, down, up, down"};
	private static String[] games = {"GM1", "GT2"};
	
	@BeforeClass
	public static void initClass() throws SQLException {
		connMock = Mockito.mock(Connection.class);
	
		rsMetaMock = Mockito.mock(ResultSetMetaData.class);
		
		Mockito.when(rsMetaMock.getColumnCount()).thenReturn(5);
		
		Mockito.when(rsMetaMock.getColumnName(1)).thenReturn("id");
		Mockito.when(rsMetaMock.getColumnName(2)).thenReturn("name");
		Mockito.when(rsMetaMock.getColumnName(3)).thenReturn("appearance");
		Mockito.when(rsMetaMock.getColumnName(4)).thenReturn("powers");
		Mockito.when(rsMetaMock.getColumnName(5)).thenReturn("movePattern");
		
		Mockito.when(rsMetaMock.getColumnType(1)).thenReturn(Types.INTEGER);
		Mockito.when(rsMetaMock.getColumnType(2)).thenReturn(Types.VARCHAR);
		Mockito.when(rsMetaMock.getColumnType(3)).thenReturn(Types.VARCHAR);
		Mockito.when(rsMetaMock.getColumnType(4)).thenReturn(Types.VARCHAR);
		Mockito.when(rsMetaMock.getColumnType(5)).thenReturn(Types.VARCHAR);
		
		Mockito.when(rsMetaMock.getColumnTypeName(1)).thenReturn(JDBCType.INTEGER.getName());
		Mockito.when(rsMetaMock.getColumnTypeName(2)).thenReturn(JDBCType.VARCHAR.getName());
		Mockito.when(rsMetaMock.getColumnTypeName(3)).thenReturn(JDBCType.VARCHAR.getName());
		Mockito.when(rsMetaMock.getColumnTypeName(4)).thenReturn(JDBCType.VARCHAR.getName());
		Mockito.when(rsMetaMock.getColumnTypeName(5)).thenReturn(JDBCType.VARCHAR.getName());
	}
	
	private EnemyDao initTestGetEnemies() throws SQLException {
		
		Statement statementMock = Mockito.mock(Statement.class);
		ResultSet resultMock = Mockito.mock(ResultSet.class);
		
		Mockito.when(connMock.createStatement()).thenReturn(statementMock);
		Mockito.when(statementMock.executeQuery("SELECT * FROM Enemy;")).thenReturn(resultMock);
		Mockito.when(resultMock.getMetaData()).thenReturn(rsMetaMock);
		
		Mockito.when(resultMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultMock.getObject(1)).thenReturn(ids[0]).thenReturn(ids[1]);
		Mockito.when(resultMock.getObject(2)).thenReturn(names[0]).thenReturn(names[1]);
		Mockito.when(resultMock.getObject(3)).thenReturn(appearances[0]).thenReturn(appearances[1]);
		Mockito.when(resultMock.getObject(4)).thenReturn(powers[0]).thenReturn(powers[1]);
		Mockito.when(resultMock.getObject(5)).thenReturn(movePatterns[0]).thenReturn(movePatterns[1]);
		
		return new EnemyDao(connMock);
	}
	
	@Test
	public void testGetEnemies() throws SQLException {
		EnemyDao enemyDaoMock = initTestGetEnemies();
		List<Enemy> expectedEnemies = new ArrayList<>();
		expectedEnemies.add(new Enemy(ids[0], names[0], appearances[0], powers[0], movePatterns[0]));
		expectedEnemies.add(new Enemy(ids[1], names[1], appearances[1], powers[1], movePatterns[1]));
		
		assertEquals(expectedEnemies, enemyDaoMock.getEnemies());
	}
	
	
	private EnemyDao initTestGetEnemyById() throws SQLException {
		
		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		ResultSet resultMock = Mockito.mock(ResultSet.class);
				
		Mockito.when(connMock.prepareStatement("SELECT * FROM Enemy WHERE id = ?;")).thenReturn(preparedMock);
		Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock);
		
		Mockito.when(resultMock.getMetaData()).thenReturn(rsMetaMock);
		
		
		
		Mockito.doAnswer( invocation->{ 
			int index = 0;
			Mockito.when(resultMock.next()).thenReturn(true);
			Mockito.when(resultMock.getObject(1)).thenReturn(ids[index]);
			Mockito.when(resultMock.getObject(2)).thenReturn(names[index]);
			Mockito.when(resultMock.getObject(3)).thenReturn(appearances[index]);
			Mockito.when(resultMock.getObject(4)).thenReturn(powers[index]);
			Mockito.when(resultMock.getObject(5)).thenReturn(movePatterns[index]);
			
			return null;
			} ).when(preparedMock).setInt(1, 1);
		
		Mockito.doAnswer( invocation->{ 
			int index = 1;
			Mockito.when(resultMock.next()).thenReturn(true);
			Mockito.when(resultMock.getObject(1)).thenReturn(ids[index]);
			Mockito.when(resultMock.getObject(2)).thenReturn(names[index]);
			Mockito.when(resultMock.getObject(3)).thenReturn(appearances[index]);
			Mockito.when(resultMock.getObject(4)).thenReturn(powers[index]);
			Mockito.when(resultMock.getObject(5)).thenReturn(movePatterns[index]);
			
			return null;
			} ).when(preparedMock).setInt(1, 2);
		
		Mockito.doAnswer( invocation->{ 
			Mockito.when(resultMock.next()).thenReturn(false);
			
			return null;
			} ).when(preparedMock).setInt(1, 3);
		
		return new EnemyDao(connMock);
	}
	
	@Test
	public void testGetEnemyById() throws SQLException {
		EnemyDao enemyDaoMock = initTestGetEnemyById();
		
		Enemy[] expectedEnemy = new Enemy[3];
		for(int i = 0; i < expectedEnemy.length-1; i++) {
			expectedEnemy[i] = new Enemy(ids[i], names[i], appearances[i], powers[i], movePatterns[i]);
		}
		expectedEnemy[2] = null;
		
		for(int i = 0; i<3; i++) {
			assertEquals(expectedEnemy[i], enemyDaoMock.getEnemyById(i+1));
		}
		
		
	}
	
	private EnemyDao initTestEnemiesByGameName() throws SQLException {

		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		ResultSet resultMock = Mockito.mock(ResultSet.class);
				
		Mockito.when(connMock.prepareStatement("SELECT * FROM Enemy WHERE game_name = ?;")).thenReturn(preparedMock);
		Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock);
		
		Mockito.when(resultMock.getMetaData()).thenReturn(rsMetaMock);
		
		
		
		Mockito.doAnswer( invocation->{ 
			int index = 0;
			Mockito.when(resultMock.next()).thenReturn(true).thenReturn(false);
			Mockito.when(resultMock.getObject(1)).thenReturn(ids[index]);
			Mockito.when(resultMock.getObject(2)).thenReturn(names[index]);
			Mockito.when(resultMock.getObject(3)).thenReturn(appearances[index]);
			Mockito.when(resultMock.getObject(4)).thenReturn(powers[index]);
			Mockito.when(resultMock.getObject(5)).thenReturn(movePatterns[index]);
			
			return null;
			} ).when(preparedMock).setString(1, games[0]);
		
		Mockito.doAnswer( invocation->{ 
			int index = 1;
			Mockito.when(resultMock.next()).thenReturn(true).thenReturn(false);
			Mockito.when(resultMock.getObject(1)).thenReturn(ids[index]);
			Mockito.when(resultMock.getObject(2)).thenReturn(names[index]);
			Mockito.when(resultMock.getObject(3)).thenReturn(appearances[index]);
			Mockito.when(resultMock.getObject(4)).thenReturn(powers[index]);
			Mockito.when(resultMock.getObject(5)).thenReturn(movePatterns[index]);
			
			return null;
			} ).when(preparedMock).setString(1, games[1]);
		
		Mockito.doAnswer( invocation->{ 
			Mockito.when(resultMock.next()).thenReturn(false);
			
			return null;
			} ).when(preparedMock).setString(1, "NonExistance");
		
		return new EnemyDao(connMock);
	}
	
	@Test
	public void testGetEnemiesByGameName() throws SQLException {
		EnemyDao enemyDaoMock = initTestEnemiesByGameName();
		String newGame = "NonExistance";
		Map<String, List<GameCharacter>> characterGameMap = new HashMap<>();
		
		for(int i = 0; i<2; i++) {
			List<GameCharacter> tempChar = new ArrayList<GameCharacter>();
			tempChar.add(new Enemy(ids[i], names[i],  appearances[i], powers[i], movePatterns[i]));
			characterGameMap.put(games[i], tempChar);
		}
		
		characterGameMap.put(newGame, Collections.emptyList());
		
		assertEquals(characterGameMap.get(games[0]), enemyDaoMock.getEnemiesByGameName(games[0]));
		assertEquals(characterGameMap.get(games[1]), enemyDaoMock.getEnemiesByGameName(games[1]));
		assertEquals(characterGameMap.get(newGame), enemyDaoMock.getEnemiesByGameName(newGame));
		
	}
	
	
	public EnemyDao initTestUpdateEnemy() throws SQLException {
		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		
		PreparedStatement updateMock1 = Mockito.mock(PreparedStatement.class);
		PreparedStatement updateMock2 = Mockito.mock(PreparedStatement.class);
		PreparedStatement updateMock3 = Mockito.mock(PreparedStatement.class);
		ResultSet resultMock1 = Mockito.mock(ResultSet.class);
		ResultSet resultMock2 = Mockito.mock(ResultSet.class);
		ResultSet resultMock3 = Mockito.mock(ResultSet.class);
		
		Mockito.when(connMock.prepareStatement("UPDATE Enemy SET name = "+names[0]+" WHERE id = ?;")).thenReturn(updateMock1);
		Mockito.when(connMock.prepareStatement("UPDATE Enemy SET name = "+names[1]+" WHERE id = ?;")).thenReturn(updateMock2);
		Mockito.when(connMock.prepareStatement("UPDATE Enemy SET name = DoughNard WHERE id = ?;")).thenReturn(updateMock3);
		
		Mockito.when(connMock.prepareStatement("SELECT * FROM Enemy WHERE id = ?;")).thenReturn(preparedMock);
		
		Mockito.when(resultMock1.getMetaData()).thenReturn(rsMetaMock);
		Mockito.when(resultMock2.getMetaData()).thenReturn(rsMetaMock);
		Mockito.when(resultMock3.getMetaData()).thenReturn(rsMetaMock);
		
		Mockito.when(resultMock1.next()).thenReturn(true);
		Mockito.when(resultMock1.getObject(1)).thenReturn(ids[0]);
		Mockito.when(resultMock1.getObject(2)).thenReturn(names[0]);
		Mockito.when(resultMock1.getObject(3)).thenReturn(appearances[0]);
		Mockito.when(resultMock1.getObject(4)).thenReturn(powers[0]);
		Mockito.when(resultMock1.getObject(5)).thenReturn(movePatterns[0]);
		
		Mockito.when(resultMock2.next()).thenReturn(true);
		Mockito.when(resultMock2.getObject(1)).thenReturn(ids[1]);
		Mockito.when(resultMock2.getObject(2)).thenReturn(names[1]);
		Mockito.when(resultMock2.getObject(3)).thenReturn(appearances[1]);
		Mockito.when(resultMock2.getObject(4)).thenReturn(powers[1]);
		Mockito.when(resultMock2.getObject(5)).thenReturn(movePatterns[1]);
		
		Mockito.when(resultMock3.next()).thenReturn(false);
		
		Mockito.doAnswer( i->{
			Mockito.when(updateMock1.executeUpdate()).thenReturn(0);
			Mockito.when(updateMock2.executeUpdate()).thenReturn(1);
			Mockito.when(updateMock3.executeUpdate()).thenReturn(1);
			Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock1);
			return null;
		}).when(preparedMock).setInt(1, 1);
		
		Mockito.doAnswer( i->{
			Mockito.when(updateMock1.executeUpdate()).thenReturn(1);
			Mockito.when(updateMock2.executeUpdate()).thenReturn(0);
			Mockito.when(updateMock3.executeUpdate()).thenReturn(1);
			Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock2);
			return null;
		}).when(preparedMock).setInt(1, 2);
		
		Mockito.doAnswer( i->{
			Mockito.when(updateMock1.executeUpdate()).thenReturn(0);
			Mockito.when(updateMock2.executeUpdate()).thenReturn(0);
			Mockito.when(updateMock3.executeUpdate()).thenReturn(0);
			Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock3);
			return null;
		}).when(preparedMock).setInt(1, 3);
		
		
		
		return new EnemyDao(connMock);
	}
	
	
	@Test
	public void testUpdateEnemy() throws SQLException {
		EnemyDao enemyDaoMock = initTestUpdateEnemy();
		
		Enemy updatedEnemy = new Enemy(ids[0], names[0],  appearances[0], powers[0], movePatterns[0]);
		
		assertEquals(0, enemyDaoMock.updateEnemy(updatedEnemy));
		
		updatedEnemy.setName(names[1]);
		assertEquals(1, enemyDaoMock.updateEnemy(updatedEnemy));
		
		updatedEnemy.setName("DoughNard");
		assertEquals(1, enemyDaoMock.updateEnemy(updatedEnemy));
		
		updatedEnemy.setId(3);
		assertEquals(0, enemyDaoMock.updateEnemy(updatedEnemy));
	}
}
