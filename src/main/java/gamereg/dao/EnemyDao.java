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

import gamereg.dao.models.Enemy;
import gamereg.dao.models.GameCharacter;

public class EnemyDao {
	private Connection conn;
	private String enemyTableName = AnnotationFunctions.getTableNameFromAnnotation(Enemy.class);
	
	public EnemyDao(Connection conn) {
		this.conn = conn;
	}
	
	private Enemy rowToEnemyMapper(ResultSet row, ResultSetMetaData rsMeta) {
		Enemy tempEnemy = new Enemy();
		
		try {
			
			for(int i = 1; i<=rsMeta.getColumnCount(); i++) {
				Class<?> type = Class.forName(JavaTypeSQLTypeMapper.mapSQLToJava(rsMeta.getColumnTypeName(i)));
				
				if(AnnotationFunctions.isFieldColumnAnnotated(rsMeta.getColumnName(i), Enemy.class)){
					String fieldName = AnnotationFunctions.getFieldNameByRowName(rsMeta.getColumnName(i), Enemy.class);
				
					Method setterMethod = tempEnemy.getClass().getMethod("set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1,fieldName.length()), type);
					setterMethod.invoke(tempEnemy, type.cast(row.getObject(i)));
				}
			}
			
			
		} catch (SQLException | NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} 
		
		return tempEnemy;
	}
	
	public List<Enemy> getEnemies() {
		List<Enemy> enemies = new ArrayList<>();
		String getEnemiesStr = "SELECT * FROM "+enemyTableName+";";
		
		try(Statement getEnemies = conn.createStatement()){
			ResultSet rs = getEnemies.executeQuery(getEnemiesStr);
			ResultSetMetaData rsMeta = rs.getMetaData();
			
			while(rs.next()) {
				enemies.add(rowToEnemyMapper(rs, rsMeta));
			}
			
		}catch(SQLException e){
			System.out.println("SQLException in getEnemies(): "+e.getMessage());
			
			throw new RuntimeException(e);
		}
		return enemies;
	}
	
	public Enemy getEnemyById(int id) {
		String getEnemyStr = "SELECT * FROM "+enemyTableName+" WHERE "+AnnotationFunctions.getFieldNameByRowName("id", Enemy.class)+"=?;";
		
		try(PreparedStatement preparedGetEnemy = conn.prepareStatement(getEnemyStr)){
			preparedGetEnemy.setInt(1, id);
			
			ResultSet rs = preparedGetEnemy.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			
			return rowToEnemyMapper(rs, rsMeta);
			
		}catch(SQLException e){
			System.out.println("SQLException in getEnemyById(): "+e.getMessage());
			
			throw new RuntimeException(e);
		}
		
	}
	
	
	public List<GameCharacter> getEnemyByGameName(String gameName){
		List<GameCharacter> results = new ArrayList<>();
		
		String selectEnemiesByGameStr = "SELECT * FROM "+enemyTableName+" WHERE game_name=?";
		
		try(PreparedStatement preparedSelectEnemiesByGame = conn.prepareStatement(selectEnemiesByGameStr)){
			
			preparedSelectEnemiesByGame.setString(1, gameName);
			
			ResultSet rs = preparedSelectEnemiesByGame.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			
			while(rs.next()) {
				results.add(rowToEnemyMapper(rs, rsMeta));
			}
			
		}catch(SQLException e) {
			System.out.println("SQLException in getEnemiesByGameName(): "+e.getMessage());
			throw new RuntimeException(e);
		}
		
		return results;
	}
	
	public void addEnemy(Enemy enemy, String conceptName) {
		String name = AnnotationFunctions.getRowNameByFieldName("name", Enemy.class);
		String powers = AnnotationFunctions.getRowNameByFieldName("powers", Enemy.class);
		String appearance = AnnotationFunctions.getRowNameByFieldName("appearance", Enemy.class);
		String movePattern = AnnotationFunctions.getRowNameByFieldName("movePattern", Enemy.class);
		String game_title = AnnotationFunctions.getRowNameByFieldName("game_name", Enemy.class);
		
		String insertPlayerStr = "INSERT INTO "+enemyTableName+"("+name+","
				+powers+","+appearance+","+movePattern+","+game_title+") VALUES (?, ?, ?, ?, ?)";
		
		try(PreparedStatement preparedInsert = conn.prepareStatement(insertPlayerStr)){
			preparedInsert.setString(1, enemy.getName());
			preparedInsert.setString(2, enemy.getPowers());
			preparedInsert.setString(3, enemy.getAppearance());
			preparedInsert.setString(4, enemy.getMovePattern());
			preparedInsert.setString(5, conceptName);
			
			preparedInsert.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLException in addEnemy(): "+e.getMessage());
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Updates enemy with the new values, cannot change enemy id
	 * @param updatedPlayer
	 */
	public void updateEnemy(Enemy updatedEnemy) {
		StringBuffer updateStr = new StringBuffer("UPDATE "+enemyTableName+" SET");
		
		String name = AnnotationFunctions.getRowNameByFieldName("name", Enemy.class);
		String powers = AnnotationFunctions.getRowNameByFieldName("powers", Enemy.class);
		String appearance = AnnotationFunctions.getRowNameByFieldName("appearance", Enemy.class);
		String movePattern = AnnotationFunctions.getRowNameByFieldName("movePattern", Enemy.class);
		
		Enemy originalEnemy = getEnemyById(updatedEnemy.getId());
		
		boolean isNameUpdated, isPowersUpdated, isAppearanceUpdated, isMovePatternUpdated;
		isNameUpdated = isPowersUpdated = isAppearanceUpdated = isMovePatternUpdated = false;
		
		if(!updatedEnemy.getName().equals(originalEnemy.getName())) {
			updateStr.append(name+"="+updatedEnemy.getName());
			isNameUpdated = true;
		}
		
		if(!updatedEnemy.getPowers().equals(originalEnemy.getPowers())) {
			if(isNameUpdated) {
				updateStr.append(", ");
			}
			updateStr.append(powers+"="+updatedEnemy.getPowers());
			isPowersUpdated = true;
		}
		
		if(!updatedEnemy.getAppearance().equals(originalEnemy.getAppearance())) {
			if(isNameUpdated || isPowersUpdated) {
				updateStr.append(", ");
			}
			updateStr.append(appearance+"="+updatedEnemy.getAppearance());
			isAppearanceUpdated = true;
		}
		
		if(!updatedEnemy.getMovePattern().equals(originalEnemy.getMovePattern())) {
			if(isNameUpdated || isPowersUpdated 
					|| isAppearanceUpdated) {
				updateStr.append(", ");
			}
			updateStr.append(movePattern+"="+updatedEnemy.getMovePattern());
			isMovePatternUpdated = true;
		}
		
		if(!(isNameUpdated && isPowersUpdated && isAppearanceUpdated && isMovePatternUpdated)) {
			System.out.println("Nothing changed, returning");
			return;
		}else {
			updateStr.append(");");
		}
		
		try(PreparedStatement preparedUpdate = conn.prepareStatement(updateStr.toString())){
			int paramCount = 1;
			
			if(isNameUpdated) {
				preparedUpdate.setString(paramCount, updatedEnemy.getName());
				paramCount++;
			}
			if(isPowersUpdated) {
				preparedUpdate.setString(paramCount, updatedEnemy.getPowers());
				paramCount++;
			}
			if(isAppearanceUpdated) {
				preparedUpdate.setString(paramCount, updatedEnemy.getAppearance());
				paramCount++;
			}
			if(isMovePatternUpdated) {
				preparedUpdate.setString(paramCount, updatedEnemy.getMovePattern());
				paramCount++;
			}
			
			preparedUpdate.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLException in updateEnemy(): "+e.getMessage());
			throw new RuntimeException(e);
		}
		
	}
	
	
}
