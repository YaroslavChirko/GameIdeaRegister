package gamereg.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gamereg.dao.models.Concept;
import gamereg.dao.models.Enemy;
import gamereg.dao.models.GameCharacter;
import gamereg.dao.models.Player;

public class ConceptDao {

	private Connection conn;
	private String conceptsTableName = AnnotationFunctions.getTableNameFromAnnotation(Concept.class);
	private PlayerDao playerDao;
	private EnemyDao enemyDao;
	
	public ConceptDao (Connection conn) {
		this.conn = conn;
		 playerDao = new PlayerDao(this.conn);
		 enemyDao = new EnemyDao(this.conn);
		//check if table for concept exists
	}
	
	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}
	
	public void setEnemyDao(EnemyDao enemyDao) {
		this.enemyDao = enemyDao;
	}
	
	public PlayerDao getPlayerDao() {
		return playerDao;
	}

	public EnemyDao getEnemyDao() {
		return enemyDao;
	}

	/**
	 * Used to map resultSet row to a Concept object
	 * 
	 * should be reworked with annotation to ensure proper naming correlation between columns and fields
	 * */
	private Concept rowToConceptMapper(ResultSet rs, ResultSetMetaData rsMeta) {
		int length;
		Concept tempConcept = new Concept();
		try {
			length = rsMeta.getColumnCount();
			Class<? extends Concept> conceptClass = tempConcept.getClass();
			
			for(int i = 1; i<=length; i++) {
				String colName = rsMeta.getColumnName(i);
				JDBCType colType = JDBCType.valueOf(rsMeta.getColumnType(i));
				//TODO perhaps create ColumnSetter(columnName="name") annotation
				
				Class<?> type = JavaTypeSQLTypeMapper.mapSQLToJava(colType.getName());
				//TODO should also add check for genre to cast properly
				String fieldName = AnnotationFunctions.getFieldNameByRowName(colName, Concept.class);
				Method fieldSetter = conceptClass.getMethod("set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1,fieldName.length()), type);
				/*used to set the corresponding field properly, if no setter found i.e. no provided or called something else it is skipped,
				this way only concepts but not their characters would be read*/ 
				if(fieldSetter != null) {				
					fieldSetter.invoke(tempConcept, type.cast(rs.getObject(i)));
				}	
			}
			
			
		} catch (SQLException | IllegalAccessException | IllegalArgumentException 
				| InvocationTargetException | NoSuchMethodException 
				| SecurityException e) {
			e.printStackTrace();
		}
		return tempConcept;
		
	}
	
	
	
	public List<Concept> getConcepts(){
		List<Concept> concepts = new ArrayList<>();
		
		
		String selectConceptsStr = "SELECT * FROM "+conceptsTableName+";";
		
		try(PreparedStatement selectStatement = conn.prepareStatement(selectConceptsStr)){
			ResultSet rs = selectStatement.executeQuery();
			
			ResultSetMetaData rsMeta = rs.getMetaData(); 
			
			
			
			while(rs.next()) {
				Concept tempConcept = rowToConceptMapper(rs, rsMeta);
				List<GameCharacter> characters  = playerDao.getPlayersByGameName(tempConcept.getTitle());
				List<GameCharacter> enemies = enemyDao.getEnemiesByGameName(tempConcept.getTitle());
				if(enemies!=null) {
					characters.addAll(enemies);
				}
				
				tempConcept.setCharacters(characters);
				concepts.add(tempConcept);
			}
			
			
			
		}catch(SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return concepts;
	}
	
	public Concept getConceptByTitle(String title) {
		String titleColumnName = AnnotationFunctions.getRowNameByFieldName("title", Concept.class);
		String getConceptByNameStr = "SELECT * FROM "+conceptsTableName+" WHERE "+titleColumnName+" = ?;";
		
		try(PreparedStatement preparedGetByName = conn.prepareStatement(getConceptByNameStr)){
			preparedGetByName.setString(1, title);
			
			ResultSet rs = preparedGetByName.executeQuery();
			
			Concept retrievedConcept = null;
			if(rs.next()) {
				retrievedConcept = rowToConceptMapper(rs, rs.getMetaData());
			
				List<GameCharacter> characters  = playerDao.getPlayersByGameName(retrievedConcept.getTitle());
				List<GameCharacter> enemies = enemyDao.getEnemiesByGameName(retrievedConcept.getTitle());
				if(enemies!=null) {
					characters.addAll(enemies);
				}
				
				retrievedConcept.setCharacters(characters);
			}
			
			return retrievedConcept;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Method to add a new concept to the table, note that game characters won't be added as they are added in a separate call
	 * @param concept
	 */
	public void addConcept(Concept concept) {
		String titleColName = AnnotationFunctions.getRowNameByFieldName("title", Concept.class);
		String descriptionColName = AnnotationFunctions.getRowNameByFieldName("description", Concept.class);
		String genreColName = AnnotationFunctions.getRowNameByFieldName("genre", Concept.class);
		
		String addConceptStr = "INSERT INTO "+conceptsTableName+" ("+ titleColName+", "
				+ descriptionColName+", "+ genreColName+") VALUES (?, ?, ?)";
		try(PreparedStatement preparedAddConcept = conn.prepareStatement(addConceptStr)){
			
			preparedAddConcept.setString(1, concept.getTitle());
			preparedAddConcept.setString(2, concept.getDescription());
			preparedAddConcept.setString(3, concept.getGenre());
			
			preparedAddConcept.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**Method to update concept information call
	 * @param originalTitle the current title of concept
	 * @param concept object of type concept with updated fields
	 * 
	 * @return number of rows altered (either 0 or 1 since the title must be original)
	 */
	public int updateConcept(String originalTitle, Concept updatedConcept) {
		//call to retrive concept
		
		Concept retrievedConcept = getConceptByTitle(originalTitle);
		if(retrievedConcept == null) {
			System.out.println("Concept wasn\'t found");
			return 0;
		}
		
		if((!updatedConcept.getTitle().equals(originalTitle)) && getConceptByTitle(updatedConcept.getTitle()) != null) {
			System.out.println("Updated title already present in the table");
			return 0;
		}
		
		
		boolean isTitleChanged = false;
		boolean isDescriptionChanged = false;
		boolean isGenreChanged = false;
		String titleColName = AnnotationFunctions.getRowNameByFieldName("title", Concept.class);
		String descriptionColName = AnnotationFunctions.getRowNameByFieldName("description", Concept.class);
		String genreColName = AnnotationFunctions.getRowNameByFieldName("genre", Concept.class);
		
		
		StringBuilder updateConceptByNameStr = new StringBuilder("UPDATE "+conceptsTableName+" SET "); 
		if(!retrievedConcept.getTitle().equals(updatedConcept.getTitle())) {
			updateConceptByNameStr.append(titleColName+" = ?");
			isTitleChanged = true;
		}
		if(!retrievedConcept.getDescription().equals(updatedConcept.getDescription())) {
			if(isTitleChanged) updateConceptByNameStr.append(", ");
			
			updateConceptByNameStr.append(descriptionColName+" = ?");
			isDescriptionChanged = true;
		}
		if(!retrievedConcept.getGenre().equals(updatedConcept.getGenre())) {
			if(isTitleChanged || isDescriptionChanged) updateConceptByNameStr.append(", ");
			
			updateConceptByNameStr.append(genreColName+" = ?");
			isGenreChanged = true;
		}
		
		if(!isTitleChanged && !isDescriptionChanged && !isGenreChanged) {
			System.out.println("No changes made, returning");
			return 0;
		}
		updateConceptByNameStr.append(" WHERE "+titleColName+" = ?;");
		try(PreparedStatement preparedUpdate = conn.prepareStatement(updateConceptByNameStr.toString())){
			int paramCount = 1;
			
			if(isTitleChanged) {
				preparedUpdate.setString(paramCount, updatedConcept.getTitle());
				paramCount++;
			}
			if(isDescriptionChanged) {
				preparedUpdate.setString(paramCount, updatedConcept.getDescription());
				paramCount++;
			}
			if(isGenreChanged) {
				preparedUpdate.setString(paramCount, updatedConcept.getGenre());
				paramCount++;
			}
			preparedUpdate.setString(paramCount, originalTitle);
			paramCount++;
			
			return preparedUpdate.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	
	public void deleteConceptByTitle(String title) {
		try(PreparedStatement preparedDelete = conn.prepareStatement("DELETE FROM "+conceptsTableName+" WHERE "+AnnotationFunctions.getRowNameByFieldName("title", Concept.class)+" = ?;")){
			preparedDelete.setString(1, title);
			
			preparedDelete.executeUpdate();
		}catch(SQLException e) {
			System.out.println("SQLException in deleteConceptByTitle(): "+e.getMessage());
			throw new RuntimeException(e);
		}
		
	}
	

	//add call to player and enemy dao to add them to this concept
	public void addPlayer(Player player, String conceptName) {
		playerDao.addPlayer(player, conceptName);
	}
	
	public void addEnemy(Enemy enemy, String conceptName) {
		enemyDao.addEnemy(enemy, conceptName);
	}
	
	
}
