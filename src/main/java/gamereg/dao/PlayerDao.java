package gamereg.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gamereg.dao.models.Player;

public class PlayerDao {
	private Connection conn;
	private String playerTableName = AnnotationFunctions.getTableNameFromAnnotation(Player.class);
	
	public PlayerDao(Connection conn) {
		this.conn = conn;
	}
	
	private Player rowToPlayerMapper(ResultSet row, ResultSetMetaData rsMeta) {
		Player tempPlayer = new Player();
		
		try {
			
			for(int i = 0; i<rsMeta.getColumnCount(); i++) {
				Class<?> type = Class.forName(JavaTypeSQLTypeMapper.mapSQLToJava(rsMeta.getColumnTypeName(i)));
				String fieldName = AnnotationFunctions.getFieldNameByRowName(rsMeta.getColumnName(i), tempPlayer);
				Method setterMethod = tempPlayer.getClass().getMethod("get"+fieldName, type);
				setterMethod.invoke(tempPlayer, type.cast(row.getObject(i)));
			}
			
			
		} catch (SQLException | NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} 
		
		return tempPlayer;
	}
	
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<>();
		String getPlayersStr = "SELECT * FROM "+playerTableName+";";
		
		try(Statement getPlayers = conn.createStatement()){
			ResultSet rs = getPlayers.executeQuery(getPlayersStr);
			ResultSetMetaData rsMeta = rs.getMetaData();
			
			while(rs.next()) {
				players.add(rowToPlayerMapper(rs, rsMeta));
			}
			
		}catch(SQLException e){
			System.out.println("SQLException in getPlayers(): "+e.getMessage());
			
			throw new RuntimeException(e);
		}
		return players;
	}
	
	public Player getPlayerById(int id) {
		String getPlayerStr = "SELECT * FROM "+playerTableName+" WHERE "+AnnotationFunctions.getFieldNameByRowName("id", Player.class)+"=?;";
		
		try(PreparedStatement preparedGetPlayer = conn.prepareStatement(getPlayerStr)){
			preparedGetPlayer.setInt(1, id);
			
			ResultSet rs = preparedGetPlayer.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			
			return rowToPlayerMapper(rs, rsMeta);
			
		}catch(SQLException e){
			System.out.println("SQLException in getPlayers(): "+e.getMessage());
			
			throw new RuntimeException(e);
		}
		
	}
	
	public void addPlayer(Player player, String conceptName) {
		String name = AnnotationFunctions.getRowNameByFieldName("name", player);
		String powers = AnnotationFunctions.getRowNameByFieldName("powers", player);
		String appearance = AnnotationFunctions.getRowNameByFieldName("appearance", player);
		String story = AnnotationFunctions.getRowNameByFieldName("story", player);
		String game_title = AnnotationFunctions.getRowNameByFieldName("game_name", player);
		
		String insertPlayerStr = "INSERT INTO "+playerTableName+"("+name+","
				+powers+","+appearance+","+story+","+game_title+") VALUES (?, ?, ?, ?, ?)";
		
		try(PreparedStatement preparedInsert = conn.prepareStatement(insertPlayerStr)){
			preparedInsert.setString(1, player.getName());
			preparedInsert.setString(2, player.getPowers());
			preparedInsert.setString(3, player.getAppearance());
			preparedInsert.setString(4, player.getStory());
			preparedInsert.setString(5, conceptName);
			
			preparedInsert.executeUpdate();
		}catch(SQLException e) {
			System.out.println("");
		}
		
	}
	
	/**
	 * Updates player with the new values, cannot change player id
	 * @param updatedPlayer
	 */
	public void updatePlayer(Player updatedPlayer) {
		
	}
	
	
}
