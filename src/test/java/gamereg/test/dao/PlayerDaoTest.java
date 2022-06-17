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

import gamereg.dao.PlayerDao;
import gamereg.dao.models.GameCharacter;
import gamereg.dao.models.Player;

//enemy dao is similar therefore it'll be copypasted from this one
public class PlayerDaoTest {
	private static Connection connMock;
	private static ResultSetMetaData rsMetaMock;
	
	private static int[] ids = {1, 2};
	private static String[] names= {"Josh", "Matthew"};
	private static String[] powers= {"higher jumps", "3 punch combo"};
	private static String[] appearances= {"tall dark haired young guy", "buff bald middle-aged dude"};
	private static String[] stories= {"not yet", "you really wanna know?"};
	private static String[] games = {"GM1", "GT2"};
	
	@BeforeClass
	public static void initClass() throws SQLException {
		connMock = Mockito.mock(Connection.class);
	
		rsMetaMock = Mockito.mock(ResultSetMetaData.class);
		
		Mockito.when(rsMetaMock.getColumnCount()).thenReturn(5);
		
		Mockito.when(rsMetaMock.getColumnName(1)).thenReturn("id");
		Mockito.when(rsMetaMock.getColumnName(2)).thenReturn("name");
		Mockito.when(rsMetaMock.getColumnName(3)).thenReturn("powers");
		Mockito.when(rsMetaMock.getColumnName(4)).thenReturn("appearance");
		Mockito.when(rsMetaMock.getColumnName(5)).thenReturn("story");
		
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
	
	private PlayerDao initTestGetPlayers() throws SQLException {
		
		Statement statementMock = Mockito.mock(Statement.class);
		ResultSet resultMock = Mockito.mock(ResultSet.class);
		
		Mockito.when(connMock.createStatement()).thenReturn(statementMock);
		Mockito.when(statementMock.executeQuery("SELECT * FROM Player;")).thenReturn(resultMock);
		Mockito.when(resultMock.getMetaData()).thenReturn(rsMetaMock);
		
		Mockito.when(resultMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultMock.getObject(1)).thenReturn(ids[0]).thenReturn(ids[1]);
		Mockito.when(resultMock.getObject(2)).thenReturn(names[0]).thenReturn(names[1]);
		Mockito.when(resultMock.getObject(3)).thenReturn(powers[0]).thenReturn(powers[1]);
		Mockito.when(resultMock.getObject(4)).thenReturn(appearances[0]).thenReturn(appearances[1]);
		Mockito.when(resultMock.getObject(5)).thenReturn(stories[0]).thenReturn(stories[1]);
		
		return new PlayerDao(connMock);
	}
	
	@Test
	public void testGetPlayers() throws SQLException {
		PlayerDao playerDaoMock = initTestGetPlayers();
		
		List<Player> expectedPlayers = new ArrayList<>();
		expectedPlayers.add(new Player(ids[0], names[0], powers[0], appearances[0], stories[0]));
		expectedPlayers.add(new Player(ids[1], names[1], powers[1], appearances[1], stories[1]));
		
		assertEquals(expectedPlayers, playerDaoMock.getPlayers());
	}
	
	
	private PlayerDao initTestGetPlayerById() throws SQLException {
		
		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		ResultSet resultMock = Mockito.mock(ResultSet.class);
				
		Mockito.when(connMock.prepareStatement("SELECT * FROM Player WHERE id = ?;")).thenReturn(preparedMock);
		Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock);
		
		Mockito.when(resultMock.getMetaData()).thenReturn(rsMetaMock);
		
		
		
		Mockito.doAnswer( invocation->{ 
			int index = 0;
			Mockito.when(resultMock.next()).thenReturn(true);
			Mockito.when(resultMock.getObject(1)).thenReturn(ids[index]);
			Mockito.when(resultMock.getObject(2)).thenReturn(names[index]);
			Mockito.when(resultMock.getObject(3)).thenReturn(powers[index]);
			Mockito.when(resultMock.getObject(4)).thenReturn(appearances[index]);
			Mockito.when(resultMock.getObject(5)).thenReturn(stories[index]);
			
			return null;
			} ).when(preparedMock).setInt(1, 1);
		
		Mockito.doAnswer( invocation->{ 
			int index = 1;
			Mockito.when(resultMock.next()).thenReturn(true);
			Mockito.when(resultMock.getObject(1)).thenReturn(ids[index]);
			Mockito.when(resultMock.getObject(2)).thenReturn(names[index]);
			Mockito.when(resultMock.getObject(3)).thenReturn(powers[index]);
			Mockito.when(resultMock.getObject(4)).thenReturn(appearances[index]);
			Mockito.when(resultMock.getObject(5)).thenReturn(stories[index]);
			
			return null;
			} ).when(preparedMock).setInt(1, 2);
		
		Mockito.doAnswer( invocation->{ 
			Mockito.when(resultMock.next()).thenReturn(false);
			
			return null;
			} ).when(preparedMock).setInt(1, 3);
		
		return new PlayerDao(connMock);
	}
	
	@Test
	public void testGetPlayerById() throws SQLException {
		PlayerDao playerDaoMock = initTestGetPlayerById();
		
		Player[] expectedPlayer = new Player[3];
		for(int i = 0; i < expectedPlayer.length-1; i++) {
			expectedPlayer[i] = new Player(ids[i], names[i], powers[i], appearances[i], stories[i]);
		}
		expectedPlayer[2] = null;
		
		for(int i = 0; i<3; i++) {
			assertEquals(expectedPlayer[i], playerDaoMock.getPlayerById(i+1));
		}
		
		
	}
	
	private PlayerDao initTestPlayersByGameName() throws SQLException {

		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		ResultSet resultMock = Mockito.mock(ResultSet.class);
				
		Mockito.when(connMock.prepareStatement("SELECT * FROM Player WHERE game_name = ?;")).thenReturn(preparedMock);
		Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock);
		
		Mockito.when(resultMock.getMetaData()).thenReturn(rsMetaMock);
		
		
		
		Mockito.doAnswer( invocation->{ 
			int index = 0;
			Mockito.when(resultMock.next()).thenReturn(true).thenReturn(false);
			Mockito.when(resultMock.getObject(1)).thenReturn(ids[index]);
			Mockito.when(resultMock.getObject(2)).thenReturn(names[index]);
			Mockito.when(resultMock.getObject(3)).thenReturn(powers[index]);
			Mockito.when(resultMock.getObject(4)).thenReturn(appearances[index]);
			Mockito.when(resultMock.getObject(5)).thenReturn(stories[index]);
			
			return null;
			} ).when(preparedMock).setString(1, games[0]);
		
		Mockito.doAnswer( invocation->{ 
			int index = 1;
			Mockito.when(resultMock.next()).thenReturn(true).thenReturn(false);
			Mockito.when(resultMock.getObject(1)).thenReturn(ids[index]);
			Mockito.when(resultMock.getObject(2)).thenReturn(names[index]);
			Mockito.when(resultMock.getObject(3)).thenReturn(powers[index]);
			Mockito.when(resultMock.getObject(4)).thenReturn(appearances[index]);
			Mockito.when(resultMock.getObject(5)).thenReturn(stories[index]);
			
			return null;
			} ).when(preparedMock).setString(1, games[1]);
		
		Mockito.doAnswer( invocation->{ 
			Mockito.when(resultMock.next()).thenReturn(false);
			
			return null;
			} ).when(preparedMock).setString(1, "NonExistance");
		
		return new PlayerDao(connMock);
	}
	
	@Test
	public void testGetPlayersByGameName() throws SQLException {
		PlayerDao playerDaoMock = initTestPlayersByGameName();
		String newGame = "NonExistance";
		Map<String, List<GameCharacter>> characterGameMap = new HashMap<>();
		
		for(int i = 0; i<2; i++) {
			List<GameCharacter> tempChar = new ArrayList<GameCharacter>();
			tempChar.add(new Player(ids[i], names[i], powers[i], appearances[i], stories[i]));
			characterGameMap.put(games[i], tempChar);
		}
		characterGameMap.put(newGame, Collections.emptyList());
		
		assertEquals(characterGameMap.get(games[0]), playerDaoMock.getPlayersByGameName(games[0]));
		assertEquals(characterGameMap.get(games[1]), playerDaoMock.getPlayersByGameName(games[1]));
		assertEquals(characterGameMap.get(newGame), playerDaoMock.getPlayersByGameName(newGame));
		
	}
	
	
	public PlayerDao initTestUpdatePlayer() throws SQLException {
		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		
		PreparedStatement updateMock1 = Mockito.mock(PreparedStatement.class);
		PreparedStatement updateMock2 = Mockito.mock(PreparedStatement.class);
		PreparedStatement updateMock3 = Mockito.mock(PreparedStatement.class);
		ResultSet resultMock1 = Mockito.mock(ResultSet.class);
		ResultSet resultMock2 = Mockito.mock(ResultSet.class);
		ResultSet resultMock3 = Mockito.mock(ResultSet.class);
		
		Mockito.when(connMock.prepareStatement("UPDATE Player SET name = "+names[0]+" WHERE id = ?;")).thenReturn(updateMock1);
		Mockito.when(connMock.prepareStatement("UPDATE Player SET name = "+names[1]+" WHERE id = ?;")).thenReturn(updateMock2);
		Mockito.when(connMock.prepareStatement("UPDATE Player SET name = Bronson WHERE id = ?;")).thenReturn(updateMock3);
		
		Mockito.when(connMock.prepareStatement("SELECT * FROM Player WHERE id = ?;")).thenReturn(preparedMock);
		
		Mockito.when(resultMock1.getMetaData()).thenReturn(rsMetaMock);
		Mockito.when(resultMock2.getMetaData()).thenReturn(rsMetaMock);
		Mockito.when(resultMock3.getMetaData()).thenReturn(rsMetaMock);
		
		Mockito.when(resultMock1.next()).thenReturn(true);
		Mockito.when(resultMock1.getObject(1)).thenReturn(ids[0]);
		Mockito.when(resultMock1.getObject(2)).thenReturn(names[0]);
		Mockito.when(resultMock1.getObject(3)).thenReturn(powers[0]);
		Mockito.when(resultMock1.getObject(4)).thenReturn(appearances[0]);
		Mockito.when(resultMock1.getObject(5)).thenReturn(stories[0]);
		
		Mockito.when(resultMock2.next()).thenReturn(true);
		Mockito.when(resultMock2.getObject(1)).thenReturn(ids[1]);
		Mockito.when(resultMock2.getObject(2)).thenReturn(names[1]);
		Mockito.when(resultMock2.getObject(3)).thenReturn(powers[1]);
		Mockito.when(resultMock2.getObject(4)).thenReturn(appearances[1]);
		Mockito.when(resultMock2.getObject(5)).thenReturn(stories[1]);
		
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
		
		
		
		return new PlayerDao(connMock);
	}
	
	
	@Test
	public void testUpdatePlayer() throws SQLException {
		PlayerDao playerDaoMock = initTestUpdatePlayer();
		
		Player updatedPlayer = new Player(ids[0], names[0], powers[0], appearances[0], stories[0]);
		
		assertEquals(0, playerDaoMock.updatePlayer(updatedPlayer));
		
		updatedPlayer.setName(names[1]);
		assertEquals(1, playerDaoMock.updatePlayer(updatedPlayer));
		
		updatedPlayer.setName("Bronson");
		assertEquals(1, playerDaoMock.updatePlayer(updatedPlayer));
	}
}
