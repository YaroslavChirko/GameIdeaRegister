package gamereg.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionLayer {
private Connection conn;
	
	
	public ConnectionLayer (String pathToProperties) {
		//read values from property file
		Properties dbProps = new Properties();
		
		
		try{
			dbProps.load(new FileReader(new File(pathToProperties)));

			Class.forName(dbProps.getProperty("driver"));
			this.conn = DriverManager.getConnection(dbProps.getProperty("url"),
					dbProps.getProperty("username"),
					dbProps.getProperty("pass"));
			TableInitializer.initTables(conn);
		}catch(IOException | ClassNotFoundException | SQLException e){
			
			System.out.println("Exception occured: "+e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	
	
	public Connection getConn() {
		return conn;
	}
	
}
