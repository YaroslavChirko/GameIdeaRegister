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

import gamereg.dao.models.GameCharacter;
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
			
			for(int i = 1; i<=rsMeta.getColumnCount(); i++) {
				Class<?> type = JavaTypeSQLTypeMapper.mapSQLToJava(rsMeta.getColumnTypeName(i));
				if(AnnotationFunctions.isFieldColumnAnnotated(rsMeta.getColumnName(i), Player.class)){
					String fieldName = AnnotationFunctions.getFieldNameByRowName(rsMeta.getColumnName(i), Player.class);
					Method setterMethod = tempPlayer.getClass().getMethod("set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1,fieldName.length()), type);
					
					setterMethod.setAccessible(true);
					if(type.equals(int.class)) {
						setterMethod.invoke(tempPlayer, (int)row.getObject(i));
					}else {
						setterMethod.invoke(tempPlayer, type.cast(row.getObject(i)));						
					}
				}
			}
			
			
		} catch (SQLException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
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
		String getPlayerStr = "SELECT * FROM "+playerTableName+" WHERE "+AnnotationFunctions.getFieldNameByRowName("id", Player.class)+" = ?;";
		
		try(PreparedStatement preparedGetPlayer = conn.prepareStatement(getPlayerStr)){
			preparedGetPlayer.setInt(1, id);
			ResultSet rs = preparedGetPlayer.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			
			Player resPlayer = null;
			if(rs.next()) {
				resPlayer = rowToPlayerMapper(rs, rsMeta);
			}
			return resPlayer;
			
		}catch(SQLException e){
			System.out.println("SQLException in getPlayerById(): "+e.getMessage());
			
			throw new RuntimeException(e);
		}
		
	}
	
	public List<GameCharacter> getPlayersByGameName(String gameName){
		List<GameCharacter> results = new ArrayList<>();
		
		String selectPlayersByGameStr = "SELECT * FROM "+playerTableName+" WHERE game_name = ?;";
		
		try(PreparedStatement preparedSelectPlayersByGame = conn.prepareStatement(selectPlayersByGameStr)){
			
			preparedSelectPlayersByGame.setString(1, gameName);
			
			ResultSet rs = preparedSelectPlayersByGame.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			
			while(rs.next()) {
				results.add(rowToPlayerMapper(rs, rsMeta));
			}
			
		}catch(SQLException e) {
			System.out.println("SQLException in getPlayersByGameName(): "+e.getMessage());
			throw new RuntimeException(e);
		}
		
		return results;
	}
	
	public void addPlayer(Player player, String conceptName) {
		String name = AnnotationFunctions.getRowNameByFieldName("name", Player.class);
		String powers = AnnotationFunctions.getRowNameByFieldName("powers", Player.class);
		String appearance = AnnotationFunctions.getRowNameByFieldName("appearance", Player.class);
		String story = AnnotationFunctions.getRowNameByFieldName("story", Player.class);
		String game_title = AnnotationFunctions.getRowNameByFieldName("game_name", Player.class);
		
		String insertPlayerStr = "INSERT INTO "+playerTableName+" ("+name+","
				+powers+","+appearance+","+story+",game_name) VALUES (?, ?, ?, ?, ?)";
		System.out.println(insertPlayerStr);
		try(PreparedStatement preparedInsert = conn.prepareStatement(insertPlayerStr)){
			preparedInsert.setString(1, player.getName());
			preparedInsert.setString(2, player.getPowers());
			preparedInsert.setString(3, player.getAppearance());
			preparedInsert.setString(4, player.getStory());
			preparedInsert.setString(5, conceptName);
			
			preparedInsert.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLException in addPlayer(): "+e.getMessage());
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Updates player with the new values, cannot change player id
	 * @param updatedPlayer
	 */
	public int updatePlayer(Player updatedPlayer) {
		StringBuffer updateStr = new StringBuffer("UPDATE "+playerTableName+" SET ");
		
		Player originalPlayer = getPlayerById(updatedPlayer.getId());
		
		if(originalPlayer == null) {
			System.out.println("No player found, returning.");
			return 0;
		}
		
		String idName = AnnotationFunctions.getRowNameByFieldName("id", Player.class);
		String name = AnnotationFunctions.getRowNameByFieldName("name", Player.class);
		String powers = AnnotationFunctions.getRowNameByFieldName("powers", Player.class);
		String appearance = AnnotationFunctions.getRowNameByFieldName("appearance", Player.class);
		String story = AnnotationFunctions.getRowNameByFieldName("story", Player.class);
		
		
		boolean isNameUpdated, isPowersUpdated, isAppearanceUpdated, isStoryUpdated;
		isNameUpdated = isPowersUpdated = isAppearanceUpdated = isStoryUpdated = false;
		
		if(!updatedPlayer.getName().equals(originalPlayer.getName())) {
			updateStr.append(name+" = ?");
			isNameUpdated = true;
		}
		
		if(!updatedPlayer.getPowers().equals(originalPlayer.getPowers())) {
			if(isNameUpdated) {
				updateStr.append(", ");
			}
			updateStr.append(powers+" = ?");
			isPowersUpdated = true;
		}
		
		if(!updatedPlayer.getAppearance().equals(originalPlayer.getAppearance())) {
			if(isNameUpdated || isPowersUpdated) {
				updateStr.append(", ");
			}
			updateStr.append(appearance+" = ?");
			isAppearanceUpdated = true;
		}
		
		if(!updatedPlayer.getStory().equals(originalPlayer.getStory())) {
			if(isNameUpdated || isPowersUpdated 
					|| isAppearanceUpdated) {
				updateStr.append(", ");
			}
			updateStr.append(story+" = ?");
			isStoryUpdated = true;
		}
		
		if(!isNameUpdated && !isPowersUpdated && !isAppearanceUpdated && !isStoryUpdated) {
			System.out.println("Nothing changed, returning");
			return 0;
		}else {
			updateStr.append(" WHERE "+idName+" = ?;");
		}
		try(PreparedStatement preparedUpdate = conn.prepareStatement(updateStr.toString())){
			int paramCount = 1;
			
			if(isNameUpdated) {
				preparedUpdate.setString(paramCount, updatedPlayer.getName());
				paramCount++;
			}
			if(isPowersUpdated) {
				preparedUpdate.setString(paramCount, updatedPlayer.getPowers());
				paramCount++;
			}
			if(isAppearanceUpdated) {
				preparedUpdate.setString(paramCount, updatedPlayer.getAppearance());
				paramCount++;
			}
			if(isStoryUpdated) {
				preparedUpdate.setString(paramCount, updatedPlayer.getStory());
				paramCount++;
			}
			
			preparedUpdate.setInt(paramCount, updatedPlayer.getId());
			
			return preparedUpdate.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLException in updatePlayer(): "+e.getMessage());
			throw new RuntimeException(e);
		}
		
	}
	
	public void deletePlayerById(int id) {
		try(PreparedStatement preparedDelete = conn.prepareStatement("DELETE FROM "+playerTableName+" WHERE "+AnnotationFunctions.getRowNameByFieldName("id", Player.class)+" = ?;")){
			
			preparedDelete.setInt(1, id);
			preparedDelete.executeUpdate();
			
		}catch(SQLException e) {
			System.out.println("SQLException in deletePlayerById(): "+e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	
}
