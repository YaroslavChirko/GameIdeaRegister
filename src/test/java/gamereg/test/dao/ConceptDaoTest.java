package gamereg.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import gamereg.dao.ConceptDao;
import gamereg.dao.EnemyDao;
import gamereg.dao.PlayerDao;


public class ConceptDaoTest {

	
	@Test
	public void testGetConcepts() throws SQLException {
		
		final String titles[] = {"MockGame1", "MockGame2", "MockGame3"};
		final String descriptions[] = {"Something about 1", "Something about 2", "Something about 3"};
		final String genres[] = {"Action", "Adventure", "Puzzle"};
		
		Connection connMock = Mockito.mock(Connection.class);
		PreparedStatement preparedMock = Mockito.mock(PreparedStatement.class);
		ResultSet resultMock = Mockito.mock(ResultSet.class);
		ResultSetMetaData rsMetaMock = Mockito.mock(ResultSetMetaData.class);
		PlayerDao playerDaoMock = Mockito.mock(PlayerDao.class);
		EnemyDao enemyDaoMock = Mockito.mock(EnemyDao.class);
		
		Mockito.when(connMock.prepareStatement("SELECT * FROM Concept;")).thenReturn(preparedMock);
		Mockito.when(preparedMock.executeQuery()).thenReturn(resultMock);
		
		//resultMock config
		Mockito.when(resultMock.getMetaData()).thenReturn(rsMetaMock);
		
		Mockito.when(resultMock.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultMock.getObject(1)).thenReturn(titles[0]).thenReturn(titles[1]).thenReturn(titles[2]);
		Mockito.when(resultMock.getObject(2)).thenReturn(descriptions[0]).thenReturn(descriptions[1]).thenReturn(descriptions[2]);//description
		Mockito.when(resultMock.getObject(3)).thenReturn(genres[0]).thenReturn(genres[1]).thenReturn(genres[2]);
		
		
		//no game characters will be returned
		Mockito.when(playerDaoMock.getPlayersByGameName(Mockito.any(String.class))).thenReturn(null);
		Mockito.when(enemyDaoMock.getEnemyByGameName(Mockito.any(String.class))).thenReturn(null);
		
		ConceptDao conceptDao = new ConceptDao(connMock);
		conceptDao.setPlayerDao(playerDaoMock);
		conceptDao.setEnemyDao(enemyDaoMock);
		
		
		
	}
}
