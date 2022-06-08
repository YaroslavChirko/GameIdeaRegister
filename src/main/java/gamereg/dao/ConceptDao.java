package gamereg.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

import gamereg.dao.models.Concept;
import gamereg.dao.models.Player;

public class ConceptDao {

	private Connection conn;
	
	public ConceptDao (Connection conn) {
		this.conn = conn;
		//check if table for concept exists
	}
	
	/*
	 * Used to map resultSet row to a Concept object
	 * 
	 * should be reworked with annotation to ensure proper naming correlation between columns and fields
	 * */
	private Concept rowToConceptMapper(ResultSet rs, ResultSetMetaData rsMeta) {
		int length;
		Concept tempConcept = new Concept();
		try {
			length = rsMeta.getColumnCount();

			Class conceptClass = tempConcept.getClass();
			//TODO rework with annotation
			Method[] methods = conceptClass.getMethods();
			
			for(int i = 0; i<length; i++) {
				String colName = rsMeta.getColumnName(i);
				Method fieldSetter = null;
				
				//looking for a method to set properties
				for(Method method : methods) {
					if(method.getName().contains("set*"+colName)) {
						fieldSetter = method;
						break;
					}
				}
				
				/*used to set the corresponding field properly, if no setter found i.e. no provided or called something else it is skipped,
				this way only concepts but not their characters would be read*/ 
				if(fieldSetter != null) {
					Class [] patrameterTypes = fieldSetter.getParameterTypes();						
					fieldSetter.invoke(fieldSetter, patrameterTypes[0].cast(rs.getObject(i)));
				}	
			}
			
			
		} catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return tempConcept;
		
	}
	
	
	
	public List<Concept> getConcepts(){
		List<Concept> concepts = new ArrayList<>();
		
		//
		String selectConceptsStr = "SELECT * FROM Concepts;";
		
		try(PreparedStatement selectStatement = conn.prepareStatement(selectConceptsStr)){
			ResultSet rs = selectStatement.executeQuery();
			
			ResultSetMetaData rsMeta = rs.getMetaData(); 
			
			
			
			while(rs.next()) {
				concepts.add(rowToConceptMapper(rs, rsMeta));
			}
			
			
			
		}catch(SQLException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return concepts;
	}
	
	public void addConcept(Concept concept) {
		//TODO change to annotation name retrieval
		String addConceptStr = "INSERT INTO Concepts (title, description, genre) VALUES (?, ?, ?)";
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
	 */
	public void updateConcept(String originalTitle, Concept updatedConcept) {
		//call to retrive concept
		
		Concept retrievedConcept = getConceptByTitle(originalTitle);
		if(retrievedConcept == null) {
			System.out.println("Concept wasn\'t found");
			return;
		}
		
		
		boolean isTitleChanged = false;
		boolean isDescriptionChanged = false;
		boolean isGenreChanged = false;
		StringBuilder updateConceptByNameStr = new StringBuilder("UPDATE Concepts SET"); 
		if(!retrievedConcept.getTitle().equals(updatedConcept.getTitle())) {
			//for now just title, get the title column name from annotation when it's added
			//TODO change to annotation name retrieval
			updateConceptByNameStr.append("title = ?");
			isTitleChanged = true;
		}
		if(!retrievedConcept.getDescription().equals(updatedConcept.getDescription())) {
			//TODO change to annotation name retrieval
			updateConceptByNameStr.append(", description = ?");
			isDescriptionChanged = true;
		}
		if(!retrievedConcept.getGenre().equals(updatedConcept.getGenre())) {
			//TODO change to annotation name retrieval
			updateConceptByNameStr.append(", genre = ?");
			isGenreChanged = true;
		}
		
		if(!(isTitleChanged || isDescriptionChanged || isGenreChanged)) {
			System.out.println("No changes made, returning");
			return;
		}
		//TODO change to annotation name retrieval
		updateConceptByNameStr.append("WHERE title = ?;");
		
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
			
			preparedUpdate.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public Concept getConceptByTitle(String title) {
		String getConceptByNameStr = "SELECT * FROM Concepts WHERE title = ?";
		
		try(PreparedStatement preparedUpdate = conn.prepareStatement(getConceptByNameStr)){
			preparedUpdate.setString(1, title);
			
			ResultSet rs = preparedUpdate.executeQuery();
			
			
			Concept retrievedConcept = rowToConceptMapper(rs, rs.getMetaData());
			
			return retrievedConcept;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	//add call to player and enemy dao to add them to this concept
	public void addPlayer(Player player) {
		
	}
	
}
