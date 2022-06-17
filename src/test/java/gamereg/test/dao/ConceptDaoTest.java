package gamereg.test.dao;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import gamereg.dao.ConceptDao;
import gamereg.dao.EnemyDao;
import gamereg.dao.PlayerDao;
import gamereg.dao.models.Concept;


public class ConceptDaoTest {

	private final static String titles[] = {"MockGame1", "MockGame2", "MockGame3"};
	private final static String descriptions[] = {"Something about 1", "Something about 2", "Something about 3"};
	private final static String genres[] = {"ACTION", "ADVENTURE", "PUZZLE"};
	private static Connection connMock;
	private static PlayerDao playerDaoMock;
	private static EnemyDao enemyDaoMock;
	private static ResultSetMetaData rsMetaMock;
	
	@BeforeClass
	public static void prepareMocks() throws SQLException {
		
		connMock = Mockito.mock(Connection.class);
		playerDaoMock = Mockito.mock(PlayerDao.class);
		enemyDaoMock = Mockito.mock(EnemyDao.class);
		
		//no game characters will be returned
		Mockito.when(playerDaoMock.getPlayersByGameName(Mockito.any(String.class))).thenReturn(null);
		Mockito.when(enemyDaoMock.getEnemiesByGameName(Mockito.any(String.class))).thenReturn(null);
		 
		rsMetaMock = Mockito.mock(ResultSetMetaData.class);
		Mockito.when(rsMetaMock.getColumnCount()).thenReturn(3);
		Mockito.when(rsMetaMock.getColumnName(1)).thenReturn("title");
		Mockito.when(rsMetaMock.getColumnType(1)).thenReturn(Types.VARCHAR);
		
		Mockito.when(rsMetaMock.getColumnName(2)).thenReturn("description");
		Mockito.when(rsMetaMock.getColumnType(2)).thenReturn(Types.VARCHAR);
		
		Mockito.when(rsMetaMock.getColumnName(3)).thenReturn("genre");
		Mockito.when(rsMetaMock.getColumnType(3)).thenReturn(Types.VARCHAR);
		
	}
	
	private ConceptDao initTestGetConcepts() throws SQLException {
		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		ResultSet resultMock = Mockito.mock(ResultSet.class);
		
		
		Mockito.when(connMock.prepareStatement("SELECT * FROM Concept;")).thenReturn(preparedMock);
		Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock);
		
		
		
		//resultMock config
		Mockito.when(resultMock.getMetaData()).thenReturn(rsMetaMock);
		
		Mockito.when(resultMock.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultMock.getObject(1)).thenReturn(titles[0]).thenReturn(titles[1]).thenReturn(titles[2]);
		Mockito.when(resultMock.getObject(2)).thenReturn(descriptions[0]).thenReturn(descriptions[1]).thenReturn(descriptions[2]);//description
		Mockito.when(resultMock.getObject(3)).thenReturn(genres[0]).thenReturn(genres[1]).thenReturn(genres[2]);
		
		
		
		
		
		
		//can make it in a longer way but don't quite want it
		ConceptDao conceptDao = new ConceptDao(connMock);
		conceptDao.setPlayerDao(playerDaoMock);
		conceptDao.setEnemyDao(enemyDaoMock);
		
		return conceptDao;
	}
	
	@Test
	public void testGetConcepts() throws SQLException {
		ConceptDao conceptDaoMock = initTestGetConcepts();
		
		List<Concept> expectedConcepts = new ArrayList<>();
		expectedConcepts.add(new Concept(titles[0], descriptions[0], genres[0], null));
		expectedConcepts.add(new Concept(titles[1], descriptions[1], genres[1], null));
		expectedConcepts.add(new Concept(titles[2], descriptions[2], genres[2], null));
	
		assertEquals(expectedConcepts, conceptDaoMock.getConcepts());
	}
	
	
	private ConceptDao initTestConceptByTitle() throws SQLException {
		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		ResultSet resultMock = Mockito.mock(ResultSet.class);
		
		
		Mockito.when(connMock.prepareStatement("SELECT * FROM Concept WHERE title = ?;")).thenReturn(preparedMock); //won't function properly due to preparedStatement
		
		Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock);
		
		Mockito.when(resultMock.getMetaData()).thenReturn(rsMetaMock);
		
		Mockito.when(resultMock.next()).thenReturn(true).thenReturn(false);
		Mockito.when(resultMock.getObject(1)).thenReturn(titles[1]).thenReturn(null);
		Mockito.when(resultMock.getObject(2)).thenReturn(descriptions[1]).thenReturn(null);
		Mockito.when(resultMock.getObject(3)).thenReturn(genres[1]).thenReturn(null);
		
		ConceptDao conceptDao = new ConceptDao(connMock);
		conceptDao.setEnemyDao(enemyDaoMock);
		conceptDao.setPlayerDao(playerDaoMock);
		return conceptDao;
	}
	
	@Test
	public void testGetConceptByTitle() throws SQLException {
		Concept expectedConcept = new Concept(titles[1], descriptions[1], genres[1], null);
		ConceptDao conceptDaoMock = initTestConceptByTitle();
		
		assertEquals(expectedConcept, conceptDaoMock.getConceptByTitle(titles[1]));
	}
	
	
	String anotherTitle = "Another title";
	//Add methods won't be tested
	private ConceptDao initTestUpdateConcept() throws SQLException {
		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		
		Mockito.when(connMock.prepareStatement("UPDATE Concept SET title = ? WHERE title = ?;")).thenReturn(preparedMock);
		Mockito.when(connMock.prepareStatement( "SELECT * FROM Concept WHERE title = ?;")).thenReturn(preparedMock);
		
		
		ResultSet resultMock1 = Mockito.mock(ResultSet.class);
		Mockito.when(resultMock1.getMetaData()).thenReturn(rsMetaMock);
		
		Mockito.when(resultMock1.next()).thenReturn(true);
		Mockito.when(resultMock1.getObject(1)).thenReturn(titles[1]);
		Mockito.when(resultMock1.getObject(2)).thenReturn(descriptions[1]);//description
		Mockito.when(resultMock1.getObject(3)).thenReturn(genres[1]);
		
		ResultSet resultMock2 = Mockito.mock(ResultSet.class);
		Mockito.when(resultMock2.getMetaData()).thenReturn(rsMetaMock);
		
		Mockito.when(resultMock2.next()).thenReturn(true);
		Mockito.when(resultMock2.getObject(1)).thenReturn(titles[2]);
		Mockito.when(resultMock2.getObject(2)).thenReturn(descriptions[2]);//description
		Mockito.when(resultMock2.getObject(3)).thenReturn(genres[2]);
		
		ResultSet resultMock3 = Mockito.mock(ResultSet.class);
		Mockito.when(resultMock3.next()).thenReturn(false);
		Mockito.when(resultMock3.getMetaData()).thenReturn(rsMetaMock);
		
		
		Mockito.doAnswer(invocation -> {
				Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock1);
				Mockito.when(preparedMock.executeUpdate()).thenReturn(0);
				return null;
			}).when(preparedMock).setString(1, titles[1]);
		
		Mockito.doAnswer(invocation -> {
			Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock2);
			Mockito.when(preparedMock.executeUpdate()).thenReturn(0);
			return null;
		}).when(preparedMock).setString(1, titles[2]);
		
		Mockito.doAnswer(invocation -> {
			Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock3);
			Mockito.when(preparedMock.executeUpdate()).thenReturn(1);
			return null;
		}).when(preparedMock).setString(1, "Another title");
		
		
		
		ConceptDao conceptDao = new ConceptDao(connMock);
	
		conceptDao.setEnemyDao(enemyDaoMock);
		conceptDao.setPlayerDao(playerDaoMock);
		return conceptDao;
	}
	
	@Test
	public void testUpdateConcept() throws SQLException {
		ConceptDao conceptMock = initTestUpdateConcept();
		Concept updatedConcept = new Concept(titles[2], descriptions[2], genres[2], null);
		assertEquals(0, conceptMock.updateConcept(titles[2], updatedConcept));
		
		updatedConcept.setTitle(titles[1]);
		assertEquals(0, conceptMock.updateConcept(titles[2], updatedConcept));
		
		updatedConcept.setTitle(anotherTitle);
		assertEquals(1, conceptMock.updateConcept(titles[2], updatedConcept));
		
		updatedConcept.setTitle(titles[1]);
		assertEquals(0, conceptMock.updateConcept(titles[2], updatedConcept));
		
	}
}
